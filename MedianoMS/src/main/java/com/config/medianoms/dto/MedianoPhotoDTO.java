package com.config.medianoms.dto;


import com.config.medianoms.models.Photo;

import java.util.List;

public record MedianoPhotoDTO(
        String id,
        String nombre,
        Double altura,
        String email,
        List<Photo> photos
) {}
