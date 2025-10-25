package com.config.photosms.services;

import com.config.photosms.clients.MedianosClient;
import com.config.photosms.dao.IPhotosDao;
import com.config.photosms.dto.PhotoDTO;
import com.config.photosms.models.Photo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicePhoto implements IServicePhoto {

    private static final Logger logger = LoggerFactory.getLogger(ServicePhoto.class);

    @Autowired
    private IPhotosDao photosDao;

    @Autowired
    private MedianosClient medianosClient;

    @Override
    public List<Photo> getPhotos() {
        return photosDao.findAll();
    }

    @Override
    @CircuitBreaker(name = "medianosClient", fallbackMethod = "addPhotoFallback")
    @Retry(name = "medianosClient")
    public void addPhoto(PhotoDTO photoDTO) {
        logger.info("🔵 Intentando crear foto para mediano: {}", photoDTO.medianoName());

        // Validar que el mediano existe
        Boolean existe = medianosClient.medianoExiste(photoDTO.medianoName());

        if (existe == null || !existe) {
            logger.warn("⚠️ Mediano no encontrado: {}", photoDTO.medianoName());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Mediano no encontrado: " + photoDTO.medianoName()
            );
        }

        logger.info("✅ Mediano existe, creando foto...");

        // Crear la foto
        Photo nuevaFoto = new Photo();
        nuevaFoto.setPath(photoDTO.url());
        nuevaFoto.setDescription(photoDTO.descripcion());
        nuevaFoto.setMedianoName(photoDTO.medianoName());

        photosDao.save(nuevaFoto);

        logger.info("✅ Foto creada exitosamente");
    }

    // Método fallback
    public void addPhotoFallback(PhotoDTO photoDTO, Exception ex) {
        logger.error("🔴 Circuit Breaker activado o error al validar mediano: {}", photoDTO.medianoName());
        logger.error("🔴 Razón: {}", ex.getMessage());

        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Servicio de medianos no disponible temporalmente. No se pudo validar: " + photoDTO.medianoName()
        );
    }

    @Override
    public List<Photo> getPhotosByMedianoName(String medianoName) {
        return photosDao.findByMedianoName(medianoName);
    }
}