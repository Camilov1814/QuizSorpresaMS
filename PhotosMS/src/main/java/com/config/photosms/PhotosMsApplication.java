package com.config.photosms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PhotosMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotosMsApplication.class, args);
    }

}
