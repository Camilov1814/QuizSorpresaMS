package com.config.medianoms.dao;

import com.config.medianoms.models.Mediano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IMedianoDao
        extends JpaRepository<Mediano,String> {
    //SELECT * FROM MEDIANO WHERE NOMBRE=NOMBRE
    Optional<Mediano> findMedianoByName(String nombre);
}
