package com.config.photosms.controllers;

import com.config.photosms.dto.PhotoDTO;
import com.config.photosms.models.Photo;
import com.config.photosms.services.IServicePhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/photos/")
public class PhotoController {

    @Autowired
    private IServicePhoto servicePhoto;

    @GetMapping("/")
    public ResponseEntity<List<Photo>> getAllPhotos() {
        return ResponseEntity.ok(servicePhoto.getPhotos());
    }

    @PostMapping("/")
    public ResponseEntity<Void> addPhoto(@RequestBody PhotoDTO photoDTO) {
        servicePhoto.addPhoto(photoDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mediano/{medianoName}")
    public ResponseEntity<List<Photo>> getPhotosByMediano(@PathVariable String medianoName) {
        return ResponseEntity.ok(servicePhoto.getPhotosByMedianoName(medianoName));
    }
}
