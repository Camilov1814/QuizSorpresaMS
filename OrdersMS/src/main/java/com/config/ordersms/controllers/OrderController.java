package com.config.ordersms.controllers;


import com.config.ordersms.dto.OrderDTO;
import com.config.ordersms.dto.OrderResponseDTO;
import com.config.ordersms.enums.OrderStatus;
import com.config.ordersms.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(orderDTO));
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable String id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buyer/{email}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByBuyer(@PathVariable String email) {
        return ResponseEntity.ok(orderService.getOrdersByBuyerEmail(email));
    }

    @GetMapping("/mediano/{medianoName}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByMediano(@PathVariable String medianoName) {
        return ResponseEntity.ok(orderService.getOrdersByMediano(medianoName));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable String id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}