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
public class LineItemOutput {

    Integer lineItemId;
    Integer productId;
    Integer orderId;
    String properties; // description or specifications about Line Item; this was optionally implemented
    Integer quantity;
    Float price;
    Float discount;

    public LineItemOutput() {
        lineItemId = 0;
        productId = 0;
        orderId = 0;
        properties = "";
        quantity = 0;
        price = (float) 0;
        discount = (float) 0;
    }

    public LineItemOutput(LineItem lineItem) {
        this();
        lineItemId = lineItem.getLineItemId();
        productId = lineItem.getProductId();
        orderId = lineItem.getOrderId();
        properties = lineItem.getProperties();
        quantity = lineItem.getQuantity();
        price = lineItem.getPrice();
        discount = lineItem.getDiscount();
    }
}
