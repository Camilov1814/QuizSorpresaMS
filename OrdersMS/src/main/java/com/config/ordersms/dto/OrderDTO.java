package com.config.ordersms.dto;

public record OrderDTO(
        String buyerName,
        String buyerEmail,
        String medianoName,
        Integer quantity
) {}