package com.shopify.demo.models.iomodels;

import com.shopify.demo.models.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderListWrapper {
    List<OrderOutput> orders;

    public OrderListWrapper() {
        orders = new ArrayList<>();
    }

    public OrderListWrapper(List<Order> orders) {
        this();
        if(orders != null) {
            for(Order order : orders) {
                this.orders.add(new OrderOutput(order));
            }
        }
    }
}
