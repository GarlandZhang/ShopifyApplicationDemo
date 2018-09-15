package com.shopify.demo.controllers;

import com.shopify.demo.models.Order;
import com.shopify.demo.models.Shop;
import com.shopify.demo.repositories.OrderRepository;
import com.shopify.demo.repositories.ShopRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ShopRepository shopRepository;

    @PostMapping("/shop/{shopId}/order/")
    private Order createOrder(@RequestBody Order newOrder, @PathVariable Integer shopId) throws Exception {
        return updateOrderWithFlag(newOrder, -1, true, shopId);
    }

    @GetMapping("/order/all")
    private List<Order> getOrders() {
        List<Order> orders = orderRepository.getAllOrdersMin();

        if(orders == null) return new ArrayList<>();

        return orders;
    }

    @GetMapping("/order/{orderId}")
    private Order getOrder(@PathVariable Integer orderId) throws Exception {
        Order order = orderRepository.getOrderByIdMin(orderId);

        if(order == null) throw new Exception("500: Order does not exist");

        return order;
    }

    @PutMapping("/order/{orderId}")
    private Order updateOrder(@RequestBody Order updatedOrder, @PathVariable Integer orderId) throws Exception {
        return updateOrderWithFlag(updatedOrder, orderId, false, -1);
    }

    private Order updateOrderWithFlag(Order newOrder, Integer id, boolean createNewFlag, Integer shopId) throws Exception {
        Order order;

        if(createNewFlag) {
            order = new Order();
            Shop shop = shopRepository.getShopByIdMin(shopId);
            if(shop != null) {
                order.setShopId(shopId);
//                order.setShop(shop);
            } else throw new Exception("500: Shop by this id does not exist");
            order.setCreationDate(new Date(Calendar.getInstance().getTimeInMillis()));
        } else {
            order = orderRepository.getOrderByIdMin(id);
        }

        if(order == null) throw new Exception("500: Order by this id does not exist");

        order.setUpdateDate(new Date(Calendar.getInstance().getTimeInMillis()));
        orderRepository.save(order);

        return order;
    }

    @DeleteMapping("/order/{orderId}")
    private String cancelOrder(@PathVariable Integer orderId) {
        // find orderId based on customerId
        orderRepository.deleteOrderById(orderId);

        return "200: Cancelled Order";
    }
}
