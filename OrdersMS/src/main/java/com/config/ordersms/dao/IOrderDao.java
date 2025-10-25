package com.config.ordersms.dao;


import com.config.ordersms.enums.OrderStatus;
import com.config.ordersms.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderDao extends JpaRepository<Order, String> {
    List<Order> findByBuyerEmail(String buyerEmail);
    List<Order> findByMedianoName(String medianoName);
    List<Order> findByStatus(OrderStatus status);
}
