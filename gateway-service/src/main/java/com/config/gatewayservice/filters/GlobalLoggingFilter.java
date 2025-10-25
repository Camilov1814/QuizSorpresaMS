package com.gateway.gatewayms.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(GlobalLoggingFilter.class);
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Generar un trace-id único
        String traceId = UUID.randomUUID().toString();

        // Agregar el trace-id al request
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(TRACE_ID_HEADER, traceId)
                .build();

        // Log de la petición entrante
        logger.info("========================================");
        logger.info("[GATEWAY] [{}] {} {}",
                traceId,
                request.getMethod(),
                request.getURI()
        );
        logger.info("[GATEWAY] [{}] Path: {}", traceId, request.getPath());
        logger.info("========================================");

        // Continuar con el filtro
        return chain.filter(exchange.mutate().request(request).build())
                .doFinally(signalType -> {
                    // Log de la respuesta
                    logger.info("[GATEWAY] [{}] Response Status: {}",
                            traceId,
                            exchange.getResponse().getStatusCode()
                    );
                });
    }

    @Override
    public int getOrder() {
        return -1;  // Ejecutar este filtro primero
    }
}