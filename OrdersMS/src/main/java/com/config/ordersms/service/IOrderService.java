package com.config.ordersms.service;

import com.config.ordersms.dto.OrderDTO;
import com.config.ordersms.dto.OrderResponseDTO;
import com.config.ordersms.enums.OrderStatus;
import com.config.ordersms.models.Order;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    OrderResponseDTO createOrder(OrderDTO orderDTO);
    List<OrderResponseDTO> getAllOrders();
    Optional<OrderResponseDTO> getOrderById(String id);
    List<OrderResponseDTO> getOrdersByBuyerEmail(String email);
    List<OrderResponseDTO> getOrdersByMediano(String medianoName);
    OrderResponseDTO updateOrderStatus(String id, OrderStatus status);
}