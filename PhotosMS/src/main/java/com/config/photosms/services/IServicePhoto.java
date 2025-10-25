package com.config.photosms.services;

import com.config.photosms.dto.PhotoDTO;
import com.config.photosms.models.Photo;

import java.util.List;

public interface IServicePhoto {
    List<Photo> getPhotos();
    void addPhoto(PhotoDTO photo);
    List<Photo> getPhotosByMedianoName(String medianoName);  // ← Agregar este método
}