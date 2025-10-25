package com.config.photosms.dao;

import com.config.photosms.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPhotosDao extends JpaRepository<Photo,Long> {

    List<Photo> findByMedianoName(String medianoName);
}
