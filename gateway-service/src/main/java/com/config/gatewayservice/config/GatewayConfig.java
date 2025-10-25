package com.config.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Ruta para enanos-service
                .route("enanos-service", r -> r
                        .path("/enanos/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://enanos-service"))

                // Ruta para photos-service
                .route("photos-service", r -> r
                        .path("/photos/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://photos-service"))

                // Ruta para orders-service
                .route("orders-service", r -> r
                        .path("/orders/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://orders-service"))

                .build();
    }
}