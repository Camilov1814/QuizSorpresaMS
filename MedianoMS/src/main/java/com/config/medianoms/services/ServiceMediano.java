package com.config.medianoms.services;

import com.config.medianoms.Excepciones.NameException;
import com.config.medianoms.clients.PhotosClient;
import com.config.medianoms.dao.IMedianoDao;
import com.config.medianoms.dto.MedianoDTO;
import com.config.medianoms.dto.MedianoPhotoDTO;
import com.config.medianoms.models.Mediano;
import com.config.medianoms.models.Photo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceMediano implements IServiceMediano {

    private static final Logger logger = LoggerFactory.getLogger(ServiceMediano.class);

    @Autowired
    private IMedianoDao medianoDao;

    @Autowired
    private PhotosClient photosClient;

    @Override
    public List<MedianoDTO> getMedianos(){
        return medianoDao.findAll().stream().map(
                mediano -> new MedianoDTO(
                        mediano.getName(),
                        mediano.getHeight(),
                        mediano.getEmail()
                )
        ).toList();
    }

    @Override
    public Mediano addMediano(MedianoDTO m) {
        Mediano mediano = new Mediano();
        mediano.setHeight(m.altura());
        mediano.setName(m.nombre());
        mediano.setEmail(m.email());

        Mediano busqueda = medianoDao.findMedianoByName(mediano.getName())
                .orElse(null);
        if(busqueda != null) {
            throw new NameException("El mediano ya existe");
        }

        return medianoDao.save(mediano);
    }

    @Override
    public Optional<Mediano> removeMediano(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Mediano> getMedianoById(String id) {
        return medianoDao.findById(id);
    }

    @Override
    public Optional<Mediano> getByName(String nombre) {
        return medianoDao.findMedianoByName(nombre);
    }

    @Override
    @CircuitBreaker(name = "photosClient", fallbackMethod = "getMedianoPhotoFallback")
    @Retry(name = "photosClient")
    public Optional<MedianoPhotoDTO> getMedianoPhoto(String nombre) {
        logger.info("🔵 Obteniendo mediano con fotos: {}", nombre);

        Optional<Mediano> medianoOpt = medianoDao.findMedianoByName(nombre);

        if (medianoOpt.isEmpty()) {
            logger.warn("⚠️ Mediano no encontrado: {}", nombre);
            return Optional.empty();
        }

        Mediano mediano = medianoOpt.get();

        logger.info("🔵 Llamando al servicio de photos...");
        // Llamar al servicio de photos usando Feign
        List<Photo> photos = photosClient.getPhotosByMedianoName(nombre);

        logger.info("✅ Fotos obtenidas: {} fotos", photos.size());

        MedianoPhotoDTO dto = new MedianoPhotoDTO(
                mediano.getId(),
                mediano.getName(),
                (double) mediano.getHeight(),
                mediano.getEmail(),
                photos
        );

        return Optional.of(dto);
    }

    // ✅ Fallback: Devolver mediano SIN fotos (degradación elegante)
    public Optional<MedianoPhotoDTO> getMedianoPhotoFallback(String nombre, Exception ex) {
        logger.error("🔴 Circuit Breaker activado o error al obtener fotos para: {}", nombre);
        logger.error("🔴 Razón: {}", ex.getMessage());
        logger.warn("⚠️ Devolviendo mediano SIN fotos (modo degradado)");

        Optional<Mediano> medianoOpt = medianoDao.findMedianoByName(nombre);

        if (medianoOpt.isEmpty()) {
            return Optional.empty();
        }

        Mediano mediano = medianoOpt.get();

        // Devolver mediano con lista vacía de fotos
        MedianoPhotoDTO dto = new MedianoPhotoDTO(
                mediano.getId(),
                mediano.getName(),
                (double) mediano.getHeight(),
                mediano.getEmail(),
                Collections.emptyList()  // ← Sin fotos
        );

        return Optional.of(dto);
    }
}