package com.config.medianoms.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    private String id;
    private String path;
    private String description;
    private String medianoName;
    private LocalDate created;
}
