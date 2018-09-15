package com.shopify.demo.repositories;

import com.shopify.demo.models.Order;

import java.util.List;

public interface OrderRepository {
    
    Order getOrderById(Integer orderId);
    
    Order getOrderByIdAndMinify(Integer orderId);

    void save(Order order);
    
    List<Order> getAllOrders();

    List<Order> getAllOrdersAndMinify();

    List<Order> getAllByShopId(Integer shopId);

    void deleteOrderById(Integer orderId);

    List<Order> getAllByShopIdAndMinify(Integer shopId);

    Order getOrderByLineItemId(Integer lineItemId);

    Order getOrderByLineItemIdAndMinify(Integer lineItemId);
}
