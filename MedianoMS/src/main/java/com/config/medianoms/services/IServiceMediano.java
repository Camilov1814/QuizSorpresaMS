package com.config.medianoms.services;

import com.config.medianoms.dto.MedianoDTO;
import com.config.medianoms.dto.MedianoPhotoDTO;
import com.config.medianoms.models.Mediano;

import java.util.List;
import java.util.Optional;

public interface IServiceMediano {
    public List<MedianoDTO> getMedianos();
    public Mediano addMediano(MedianoDTO m);
    public Optional<Mediano> removeMediano(String id);
    public Optional<Mediano> getMedianoById(String id);
    public Optional<Mediano> getByName(String nombre);
    Optional<MedianoPhotoDTO> getMedianoPhoto(String nombre);
}
