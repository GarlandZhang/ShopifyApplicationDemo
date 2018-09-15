package com.shopify.demo.repositories;

import com.shopify.demo.models.Order;

import java.util.List;

public interface OrderRepository {
    
    Order getOrderById(Integer orderId);
    
    Order getOrderByIdMin(Integer orderId);

    void save(Order order);
    
    List<Order> getAllOrders();

    List<Order> getAllOrdersMin();

    List<Order> getAllByShopId(Integer shopId);

    void deleteOrderById(Integer orderId);

    List<Order> getAllByShopIdMin(Integer shopId);

    Order getOrderByLineItemId(Integer lineItemId);

    Order getOrderByLineItemIdMin(Integer lineItemId);
}
