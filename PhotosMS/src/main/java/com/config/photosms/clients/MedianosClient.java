package com.config.photosms.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "enanos-service")
public interface MedianosClient {

    @GetMapping("/api/medianos/existe/{nombre}")
    Boolean medianoExiste(@PathVariable String nombre);
}