package com.shopify.demo.models.iomodels;

import com.shopify.demo.models.LineItem;
import com.shopify.demo.models.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class OrderHeavyOutput {

    Integer orderId;
    Integer shopId;
    Date creationDate;
    Date updateDate;
    Float total;
    List<LineItemOutput> lineItems;

    public OrderHeavyOutput() {
        orderId = 0;
        shopId = 0;
        creationDate = new Date(Calendar.getInstance().getTimeInMillis());
        updateDate = new Date(Calendar.getInstance().getTimeInMillis());
        total = (float) 0;
        lineItems = new ArrayList<>();
    }

    public OrderHeavyOutput(Order order) {
        this();
        orderId = order.getOrderId();
        shopId = order.getShopId();
        creationDate = order.getCreationDate();
        updateDate = order.getUpdateDate();
        total = order.getTotal();

        if(order.getLineItems() != null) {
            for(LineItem lineItem : order.getLineItems()) {
                lineItems.add(new LineItemOutput(lineItem));
            }
        }
    }
}