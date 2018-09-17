package com.shopify.demo.controllers;

import com.shopify.demo.models.LineItem;
import com.shopify.demo.models.Order;
import com.shopify.demo.models.Shop;
import com.shopify.demo.repositories.LineItemRepository;
import com.shopify.demo.repositories.OrderRepository;
import com.shopify.demo.repositories.ShopRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    LineItemRepository lineItemRepository;

    /**
     * createOrder: creates new Order with properties in newOrder for Shop with id, shopId
     * @param newOrder
     * @param shopId
     * @return returns the newly created Order
     * @throws Exception
     */
    @PostMapping("/shop/{shopId}/order/create")
    private ResponseEntity<Order> createOrder(@RequestBody Order newOrder, @PathVariable Integer shopId) throws Exception {
        Order order = updateOrderWithFlag(newOrder, -1, true, shopId);

        if(order == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Create unsuccessful")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Create successful")
                .body(order);
    }

    /**
     * getOrders: ADMIN USE ONLY. Gets all Order in database
     * @return all active Orders
     */
    @GetMapping("/order/all")
    private ResponseEntity<List<Order>> getOrders() {
        List<Order> orders = orderRepository.getAllOrdersAndMinify();

        if(orders == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ArrayList<>());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(orders);
    }

    /**
     * getOrder: Get Order with id, orderId
     * @param orderId
     * @return the requested Order
     * @throws Exception
     */
    @GetMapping("/order/{orderId}")
    private ResponseEntity<Order> getOrder(@PathVariable Integer orderId) throws Exception {
        Order order = orderRepository.getOrderByIdAndMinify(orderId);

        if(order == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Order does not exist")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(order);
    }

    /**
     * getLineItemsByOrderId: gets Line Items for the requested Order with id, orderId
     * @param orderId
     * @return list of all List Items
     */
    @GetMapping("/order/{orderId}/line-item/all")
    private ResponseEntity<List<LineItem>> getLineItemsByOrderId(@PathVariable Integer orderId) {
        List<LineItem> lineItems = lineItemRepository.getAllByOrderIdAndMinify(orderId);

        if(lineItems == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ArrayList<>());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(lineItems);
    }

    /**
     * updateOrder: updates Order with id, orderId, using the properties of updatedOrder
     * @param updatedOrder
     * @param orderId
     * @return updated Order
     * @throws Exception
     */
    @PutMapping("/order/{orderId}")
    private ResponseEntity<Order> updateOrder(@RequestBody Order updatedOrder, @PathVariable Integer orderId) throws Exception {
        Order order = updateOrderWithFlag(updatedOrder, orderId, false, -1);

        if(order == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Shop or Order does not exist with input id")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(order);
    }

    /**
     * updateOrderWithFlag: updates existing Order with id using properties of newOrder; creates new Order if createNewFlag is true
     *  Current use case is limited but can be extended from adding more properties in Order model.
     * @param newOrder
     * @param id
     * @param createNewFlag
     * @param shopId
     * @return the newly created Order or updated Order
     * @throws Exception
     */
    private Order updateOrderWithFlag(Order newOrder, Integer id, boolean createNewFlag, Integer shopId) throws Exception {
        Order order;

        // if true, create new Order and set property
        if(createNewFlag) {
            order = new Order();

            // find parent Shop to verify it exists
            Shop shop = shopRepository.getShopByIdAndMinify(shopId);
            if(shop == null) return null;
            order.setShopId(shopId);

        } else {
            order = orderRepository.getOrderByIdAndMinify(id);
        }

        if(order == null) return null;

        // set new properties
        order.setUpdateDate(new Date(Calendar.getInstance().getTimeInMillis()));
        orderRepository.save(order);

        return order;
    }

    /**
     * cancelOrder: deletes Order with id, orderId
     * @param orderId
     * @return message that Order successfully cancelled
     */
    @DeleteMapping("/order/{orderId}")
    private ResponseEntity<String> cancelOrder(@PathVariable Integer orderId) {

        try{
            orderRepository.deleteOrderById(orderId);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Status", "500: Delete unsuccessful")
                    .body("Delete unsuccessful");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Delete successful")
                .body("Delete successful");
    }
}
