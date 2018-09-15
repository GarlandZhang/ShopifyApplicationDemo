package com.shopify.demo.repositories;

import com.shopify.demo.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderJPARepository extends JpaRepository<Order, Integer> {

    Order findOrderByOrderId(Integer orderId);

    List<Order> findAllByShopId(Integer shopId);
}
