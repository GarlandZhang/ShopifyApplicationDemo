package com.shopify.demo.models.iomodels;

import com.shopify.demo.models.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderListHeavyWrapper {
    List<OrderHeavyOutput> orders;

    public OrderListHeavyWrapper() {
        orders = new ArrayList<>();
    }

    public OrderListHeavyWrapper(List<Order> orders) {
        this();
        if(orders != null) {
            for(Order order : orders) {
                this.orders.add(new OrderHeavyOutput(order));
            }
        }
    }
}
