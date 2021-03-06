package com.shopify.demo.controllers;

import com.shopify.demo.models.LineItem;
import com.shopify.demo.models.Order;
import com.shopify.demo.models.Shop;
import com.shopify.demo.models.iomodels.*;
import com.shopify.demo.repositories.LineItemRepository;
import com.shopify.demo.repositories.OrderRepository;
import com.shopify.demo.repositories.ShopRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
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
     * internalCreateOrder: creates new Order with properties in newOrder for Shop with id, shopId; only for internal use
     * @param newOrder
     * @param shopId
     * @return returns the newly created Order
     * @throws Exception
     */
    public Order internalCreateOrder(OrderInput newOrder, Integer shopId, Integer userId) throws Exception {
        return updateOrderWithFlag(newOrder, -1, true, shopId, userId);
    }

    /**
     * createOrder: creates new Order with properties in newOrder for Shop with id, shopId
     * @param newOrder
     * @param shopId
     * @return returns the newly created Order
     * @throws Exception
     */
//    @PostMapping("/shop/{shopId}/order/create")
    private ResponseEntity<OrderOutput> createOrder(@RequestBody OrderInput newOrder, @PathVariable Integer shopId, Integer userId) throws Exception {
        Order order = updateOrderWithFlag(newOrder, -1, true, shopId, userId);

        if(order == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Create unsuccessful")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Create successful")
                .body(new OrderOutput(order));
    }

    /**
     * getOrdersMin: ADMIN USE ONLY. Gets all minimized Order in database
     * @return all active Orders
     */
    @GetMapping("/order/all/min")
    private ResponseEntity<OrderListWrapper> getOrdersMin() {
        List<Order> orders = orderRepository.getAllOrders();

        if(orders == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new OrderListWrapper());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new OrderListWrapper(orders));
    }

    /**
     * getOrders: ADMIN USE ONLY. Gets all Order in database
     * @return all active Orders
     */
    @GetMapping("/order/all")
    private ResponseEntity<OrderListHeavyWrapper> getOrders() {
        List<Order> orders = orderRepository.getAllOrders();

        if(orders == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new OrderListHeavyWrapper());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new OrderListHeavyWrapper(orders));
    }


    /**
     * getOrderMin: Get minimized Order with id, orderId
     * @param orderId
     * @return the requested Order
     * @throws Exception
     */
    @GetMapping("/order/{orderId}/min")
    private ResponseEntity<OrderOutput> getOrderMin(@PathVariable Integer orderId) throws Exception {
        Order order = orderRepository.getOrderById(orderId);

        if(order == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Order does not exist")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new OrderOutput(order));
    }

    /**
     * getOrder: Get Order with id, orderId
     * @param orderId
     * @return the requested Order
     * @throws Exception
     */
    @GetMapping("/order/{orderId}")
    private ResponseEntity<OrderHeavyOutput> getOrder(@PathVariable Integer orderId) throws Exception {
        Order order = orderRepository.getOrderById(orderId);

        if(order == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Order does not exist")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new OrderHeavyOutput(order));
    }

    /**
     * getLineItemsByOrderId: gets Line Items for the requested Order with id, orderId
     * @param orderId
     * @return list of all List Items
     */
    @GetMapping("/order/{orderId}/line-item/all")
    private ResponseEntity<LineItemListWrapper> getLineItemsByOrderId(@PathVariable Integer orderId) {
        List<LineItem> lineItems = lineItemRepository.getAllByOrderId(orderId);

        if(lineItems == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new LineItemListWrapper());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new LineItemListWrapper(lineItems));
    }

    /**
     * updateOrder: updates Order with id, orderId, using the properties of updatedOrder
     * @param updatedOrder
     * @param orderId
     * @return updated Order
     * @throws Exception
     */
    @PutMapping("/order/{orderId}/secure")
    private ResponseEntity<OrderHeavyOutput> updateOrder(@RequestBody OrderInput updatedOrder, @PathVariable Integer orderId, @RequestHeader String authorization, Integer userId) throws Exception {

        // security check to make sure
        try {
            // parse token
            Claims body = Jwts.parser()
                    .setSigningKey("secret")
                    .parseClaimsJws(authorization)
                    .getBody(); // parse then get body of request

            Order order = orderRepository.getOrderById(orderId);

            if(order == null || order.getUserId() != Integer.parseInt((String)body.get("userId")))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .header("Status", "401: Unauthorized")
                        .body(null);

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Status", "400: Invalid Token")
                    .body(null);
        }

        Order order = updateOrderWithFlag(updatedOrder, orderId, false, -1, userId);

        if(order == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Shop or Order does not exist with input id")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new OrderHeavyOutput(order));
    }

    /**
     * validOrder: validates whether updatedOrder is a valid OrderInput
     * @param updatedOrder
     * @return
     */
    private boolean validOrder(OrderInput updatedOrder) {
        return updatedOrder != null
            && updatedOrder.getStatus() != null
            && (updatedOrder.getStatus().equals("INCOMPLETE")
            ||  updatedOrder.getStatus().equals("COMPLETE")
            ||  updatedOrder.getStatus().equals("CANCELLED"));
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
    private Order updateOrderWithFlag(OrderInput newOrder, Integer id, boolean createNewFlag, Integer shopId, Integer userId) throws Exception {

        if(!validOrder(newOrder)) return null;

        Order order;

        // if true, create new Order and set property
        if(createNewFlag) {
            order = new Order();

            // find parent Shop to verify it exists
            Shop shop = shopRepository.getShopById(shopId);
            if(shop == null) return null;
            order.setShopId(shopId);
            order.setCreationDate(new Date(Calendar.getInstance().getTimeInMillis()));
            order.setUserId(userId);

        } else {
            order = orderRepository.getOrderById(id);
        }

        if(order == null) return null;

        // set new properties
        order.setStatus(newOrder.getStatus());
        order.setUpdateDate(new Date(Calendar.getInstance().getTimeInMillis()));
        order = orderRepository.saveOrder(order);

        return order;
    }

    /**
     * cancelOrder: deletes Order with id, orderId
     * @param orderId
     * @return message that Order successfully cancelled
     */
    @DeleteMapping("/order/{orderId}/secure")
    private ResponseEntity<Message> cancelOrder(@PathVariable Integer orderId, @RequestHeader String authorization) {

        // security check to make sure
        try {
            // parse token
            Claims body = Jwts.parser()
                    .setSigningKey("secret")
                    .parseClaimsJws(authorization)
                    .getBody(); // parse then get body of request

            Order order = orderRepository.getOrderById(orderId);

            if(order == null || order.getUserId() != Integer.parseInt((String)body.get("userId")))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .header("Status", "401: Unauthorized")
                        .body(null);

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Status", "400: Invalid Token")
                    .body(null);
        }

        try{
            orderRepository.deleteOrderById(orderId);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Status", "400: Delete unsuccessful")
                    .body(new Message("Delete unsuccessful"));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Delete successful")
                .body(new Message("Delete successful"));
    }
}
