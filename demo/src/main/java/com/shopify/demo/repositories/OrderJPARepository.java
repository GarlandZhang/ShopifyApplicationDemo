package com.shopify.demo.repositories;

import com.shopify.demo.models.Order;
import com.shopify.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderJPARepository extends JpaRepository<Order, Integer> {

    Order findOrderByOrderId(Integer orderId);

    List<Order> findAllByShopId(Integer shopId);

}
