package com.shopify.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@AllArgsConstructor
@Getter
@Setter
public class LineItem {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer lineItemId;
    Integer productId;
    Integer orderId;
    String properties; // description or specifications about Line Item; this was optionally implemented
    Integer quantity;
    Float price;
    Float discount;

    @ManyToOne
    @JoinColumn(name="orderId", insertable = false, updatable = false)
    Order order;

    @ManyToOne
    @JoinColumn(name="productId", insertable = false, updatable = false)
    Product product;

    public LineItem() {
        // initialize default values
        properties = "";
        quantity = 0;
        price = (float)0;
        discount = (float)0;
    }
}
