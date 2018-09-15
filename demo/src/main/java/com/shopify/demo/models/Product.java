package com.shopify.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer productId;
    String name;
    String description;
    Float price;
    Integer shopIdVal;

    @ManyToOne
    @JoinColumn(name="shopIdVal", insertable=false, updatable=false)
    Shop shop;
}
