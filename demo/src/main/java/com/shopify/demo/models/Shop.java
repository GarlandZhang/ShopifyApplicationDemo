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
public class Shop {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "shopId", nullable = false, updatable = false)
    Integer shopId;
    String name;
    Integer vendorId;
    String description;

    @OneToMany(mappedBy="shop", cascade = CascadeType.ALL)
    List<Order> orders;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    List<Product> products;
}
