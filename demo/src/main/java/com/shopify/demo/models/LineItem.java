package com.shopify.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LineItem {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Integer lineItemId;
    Integer productId;
    Integer orderId;
    String properties;
    Integer quantity;
    Float price;
    Float discount;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="orderId", insertable = false, updatable = false)
    Order order;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="productId", insertable = false, updatable = false)
    Product product;
}
