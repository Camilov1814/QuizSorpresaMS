package com.config.ordersms.dto;


import com.config.ordersms.enums.OrderStatus;

public record OrderResponseDTO(
        String id,
        String buyerName,
        String buyerEmail,
        String medianoName,
        Integer quantity,
        Double totalPrice,
        OrderStatus status,
        String createdAt
) {}