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
    Integer userId;
    Date creationDate;
    Date updateDate;
    String status;
    Float total;
    List<LineItemOutput> lineItems;

    public OrderHeavyOutput() {
        orderId = 0;
        shopId = 0;
        userId = 0;
        creationDate = new Date(Calendar.getInstance().getTimeInMillis());
        updateDate = new Date(Calendar.getInstance().getTimeInMillis());
        total = (float) 0;
        lineItems = new ArrayList<>();
        status = "INCOMPLETE";
    }

    public OrderHeavyOutput(Order order) {
        this();
        orderId = order.getOrderId();
        shopId = order.getShopId();
        userId = order.getUserId();
        creationDate = order.getCreationDate();
        updateDate = order.getUpdateDate();
        total = order.getTotal();
        status = order.getStatus();

        if(order.getLineItems() != null) {
            for(LineItem lineItem : order.getLineItems()) {
                lineItems.add(new LineItemOutput(lineItem));
            }
        }
    }
}
