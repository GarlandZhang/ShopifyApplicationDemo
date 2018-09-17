package com.shopify.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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
    Integer shopId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shopId", insertable=false, updatable=false)
    Shop shop;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<LineItem> lineItems;

}
