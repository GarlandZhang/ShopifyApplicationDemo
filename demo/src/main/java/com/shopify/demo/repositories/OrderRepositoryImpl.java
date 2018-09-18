package com.shopify.demo.repositories;

import com.shopify.demo.models.Order;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@NoArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    OrderJPARepository orderJPARepository;

    @Autowired
    LineItemRepository lineItemRepository;

    @Override
    public Order getOrderById(Integer orderId) {
        return orderJPARepository.findOrderByOrderId(orderId);
    }

    @Override
    public Order getOrderByIdAndMinify(Integer orderId) {
        return minify(getOrderById(orderId));
    }

    /**
     * minify: reduces size of Order object to reduce fetch load; prevents fetching for parent and children objects
     * @param order
     * @return minified Order
     */
    Order minify(Order order) {
        if(order != null) {
//            order.setCustomer(null);
            order.setShop(null);
            order.setLineItems(new ArrayList<>());
        }

        return order;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderJPARepository.findAll();
    }


    @Override
    public List<Order> getAllOrdersAndMinify() {
        List<Order> orders = getAllOrders();

        if(orders != null) {
            for(Order order: orders) {
                minify(order);
            }
        }

        return orders;
    }

    @Override
    public List<Order> getAllByShopId(Integer shopId) {
        return orderJPARepository.findAllByShopId(shopId);
    }

    @Override
    public List<Order> getAllByShopIdAndMinify(Integer shopId) {
        List<Order> orders = getAllByShopId(shopId);

        // minify each Order object
        if(orders != null){
            for(Order order: orders) {
                minify(order);
            }
        }

        return orders;
    }

    @Override
    public Order getOrderByLineItemId(Integer lineItemId) {
        return orderJPARepository.findOrderByOrderId(lineItemRepository.getLineItemById(lineItemId).getOrderId());

    }

    @Override
    public Order getOrderByLineItemIdAndMinify(Integer lineItemId) {
        return minify(getOrderByLineItemId(lineItemId));
    }

    @Override
    public Order saveOrder(Order order) {
        return orderJPARepository.save(order);
    }

    @Override
    public void deleteOrderById(Integer orderId) {
        orderJPARepository.deleteById(orderId);
    }

}
