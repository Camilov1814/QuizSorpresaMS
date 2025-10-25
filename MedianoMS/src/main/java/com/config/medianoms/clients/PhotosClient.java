package com.config.medianoms.clients;

import com.config.medianoms.models.Photo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "photos-service")  // ← Este nombre debe coincidir con spring.application.name del servicio de photos
public interface PhotosClient {

    // Este método replica el endpoint del servicio de photos
    @GetMapping("/api/photos/mediano/{medianoName}")
    List<Photo> getPhotosByMedianoName(@PathVariable String medianoName);
}