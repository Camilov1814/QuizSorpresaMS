package com.config.ordersms.service;

import com.config.ordersms.clients.MedianosClient;
import com.config.ordersms.dao.IOrderDao;
import com.config.ordersms.dto.OrderDTO;
import com.config.ordersms.dto.OrderResponseDTO;
import com.config.ordersms.enums.OrderStatus;
import com.config.ordersms.models.Order;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private MedianosClient medianosClient;

    private static final Double PRECIO_POR_ENANO = 100.0;

    @Override
    @CircuitBreaker(name = "medianosClient", fallbackMethod = "createOrderFallback")
    @Retry(name = "medianosClient")
    public OrderResponseDTO createOrder(OrderDTO orderDTO) {
        logger.info("🔵 Intentando crear orden para mediano: {}", orderDTO.medianoName());

        // Validar que el mediano existe
        Boolean existe = medianosClient.medianoExiste(orderDTO.medianoName());

        if (existe == null || !existe) {
            logger.warn("⚠️ Mediano no encontrado: {}", orderDTO.medianoName());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Mediano no encontrado: " + orderDTO.medianoName()
            );
        }

        logger.info("✅ Mediano existe, creando orden...");

        // Crear la orden
        Order order = new Order();
        order.setBuyerName(orderDTO.buyerName());
        order.setBuyerEmail(orderDTO.buyerEmail());
        order.setMedianoName(orderDTO.medianoName());
        order.setQuantity(orderDTO.quantity());
        order.setTotalPrice(orderDTO.quantity() * PRECIO_POR_ENANO);
        order.setStatus(OrderStatus.PENDIENTE);

        Order savedOrder = orderDao.save(order);

        logger.info("✅ Orden creada exitosamente: {}", savedOrder.getId());

        return mapToResponseDTO(savedOrder);
    }

    // Método fallback
    public OrderResponseDTO createOrderFallback(OrderDTO orderDTO, Exception ex) {
        logger.error("🔴 Circuit Breaker activado o error al validar mediano: {}", orderDTO.medianoName());
        logger.error("🔴 Razón: {}", ex.getMessage());

        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Servicio de medianos no disponible temporalmente. No se pudo validar: " + orderDTO.medianoName()
        );
    }

    // ... resto de métodos sin cambios

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderDao.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public Optional<OrderResponseDTO> getOrderById(String id) {
        return orderDao.findById(id)
                .map(this::mapToResponseDTO);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByBuyerEmail(String email) {
        return orderDao.findByBuyerEmail(email).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByMediano(String medianoName) {
        return orderDao.findByMedianoName(medianoName).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public OrderResponseDTO updateOrderStatus(String id, OrderStatus status) {
        Order order = orderDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Orden no encontrada"
                ));

        order.setStatus(status);
        Order updatedOrder = orderDao.save(order);

        return mapToResponseDTO(updatedOrder);
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getBuyerName(),
                order.getBuyerEmail(),
                order.getMedianoName(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getCreatedAt().toString()
        );
    }
}