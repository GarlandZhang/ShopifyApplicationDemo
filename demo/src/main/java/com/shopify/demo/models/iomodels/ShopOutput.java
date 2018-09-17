package com.shopify.demo.models.iomodels;

import com.shopify.demo.models.Order;
import com.shopify.demo.models.Product;
import com.shopify.demo.models.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ShopOutput {

    Integer shopId;
    String name;
    String description;
    Integer vendorId;
    List<Integer> orders;
    List<Integer> products;

    public ShopOutput() {
        shopId = 0;
        name = "";
        description = "";
        vendorId = 0;
        orders = new ArrayList<>();
        products = new ArrayList<>();
    }

    public ShopOutput(Shop savedShop) {
        this();
        shopId = savedShop.getShopId();
        name = savedShop.getName();
        description = savedShop.getDescription();
        vendorId = savedShop.getVendorId();

        if(savedShop.getOrders() != null) {
            for(Order order: savedShop.getOrders()) {
                orders.add(order.getOrderId());
            }
        }

        if(savedShop.getProducts() != null) {
            for(Product product: savedShop.getProducts()) {
                products.add(product.getProductId());
            }
        }
    }
}
