package com.config.medianoms.controllers;

import com.config.medianoms.dto.MedianoPhotoDTO;
import com.config.medianoms.dto.MedianoDTO;
import com.config.medianoms.models.Mediano;
import com.config.medianoms.services.IServiceMediano;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medianos")
public class MedianoController {
    @Autowired
    private IServiceMediano mediano;


    @GetMapping("/")
    public List<MedianoDTO> getMedianos(){
        return mediano.getMedianos();

    }
    @PostMapping("/")
    public ResponseEntity<?> addMediano(@RequestBody @Valid
                                            MedianoDTO medianoDTO){
        System.out.println(medianoDTO);
        mediano.addMediano(medianoDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filtrado/{nombre}")
    public ResponseEntity<MedianoPhotoDTO> filtrado(@PathVariable String nombre){
        return mediano.getMedianoPhoto(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/existe/{nombre}")
    public ResponseEntity<Boolean> medianoExiste(@PathVariable String nombre){
        return ResponseEntity.ok(mediano.getByName(nombre).isPresent());
    }

}
