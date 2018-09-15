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
    private Order createOrder(@RequestBody Order newOrder, @PathVariable Integer shopId) throws Exception {
        return updateOrderWithFlag(newOrder, -1, true, shopId);
    }

    /**
     * getOrders: ADMIN USE ONLY. Gets all Order in database
     * @return all active Orders
     */
    @GetMapping("/order/all")
    private List<Order> getOrders() {
        List<Order> orders = orderRepository.getAllOrdersAndMinify();

        if(orders == null) return new ArrayList<>();

        return orders;
    }

    /**
     * getOrder: Get Order with id, orderId
     * @param orderId
     * @return the requested Order
     * @throws Exception
     */
    @GetMapping("/order/{orderId}")
    private Order getOrder(@PathVariable Integer orderId) throws Exception {
        Order order = orderRepository.getOrderByIdAndMinify(orderId);

        if(order == null) throw new Exception("500: Order does not exist");

        return order;
    }

    /**
     * getLineItemsByOrderId: gets Line Items for the requested Order with id, orderId
     * @param orderId
     * @return list of all List Items
     */
    @GetMapping("/order/{orderId}/line-item/all")
    private List<LineItem> getLineItemsByOrderId(@PathVariable Integer orderId) {
        List<LineItem> lineItems = lineItemRepository.getAllByOrderIdAndMinify(orderId);

        if(lineItems == null) return new ArrayList<>();

        return lineItems;
    }

    /**
     * updateOrder: updates Order with id, orderId, using the properties of updatedOrder
     * @param updatedOrder
     * @param orderId
     * @return updated Order
     * @throws Exception
     */
    @PutMapping("/order/{orderId}")
    private Order updateOrder(@RequestBody Order updatedOrder, @PathVariable Integer orderId) throws Exception {
        return updateOrderWithFlag(updatedOrder, orderId, false, -1);
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
            if(shop == null) throw new Exception("500: Shop by this id does not exist");
            order.setShopId(shopId);

        } else {
            order = orderRepository.getOrderByIdAndMinify(id);
        }

        if(order == null) throw new Exception("500: Order by this id does not exist");

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
    private String cancelOrder(@PathVariable Integer orderId) {
        orderRepository.deleteOrderById(orderId);

        return "200: Cancelled Order";
    }
}
