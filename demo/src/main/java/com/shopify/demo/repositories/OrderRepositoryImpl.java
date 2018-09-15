package com.shopify.demo.repositories;

import com.shopify.demo.models.Order;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@NoArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    OrderJPARepository orderJPARepository;


    @Override
    public Order getOrder(Integer orderId) {
        return orderJPARepository.findOrderByOrderId(orderId);
    }

    @Override
    public Order getOrderByIdMin(Integer orderId) {
        return minifyOrder(getOrder(orderId));
    }

    Order minifyOrder(Order order) {
        if(order != null) {
            order.setCustomer(null);
            order.setShop(null);
            order.setLineItems(null);
        }

        return order;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderJPARepository.findAll();
    }


    @Override
    public List<Order> getAllOrdersMin() {
        List<Order> orders = getAllOrders();

        if(orders != null) {
            for(Order order: orders) {
                minifyOrder(order);
            }
        }

        return orders;
    }

    @Override
    public List<Order> getAllByShopId(Integer shopId) {
        return orderJPARepository.findAllByShopId(shopId);
    }

    @Override
    public List<Order> getAllByShopIdMin(Integer shopId) {
        List<Order> orders = getAllByShopId(shopId);

        if(orders != null){
            for(Order order: orders) {
                minifyOrder(order);
            }
        }

        return orders;
    }

    @Override
    public void save(Order order) {
        orderJPARepository.save(order);
    }

    @Override
    public void deleteOrderById(Integer orderId) {
        orderJPARepository.deleteById(orderId);
    }

}
