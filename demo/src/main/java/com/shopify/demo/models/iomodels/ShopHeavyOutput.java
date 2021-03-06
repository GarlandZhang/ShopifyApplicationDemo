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
public class ShopHeavyOutput {

    Integer shopId;
    String name;
    String description;
    Integer userId;
    List<OrderHeavyOutput> orders;
    List<ProductOutput> products;

    public ShopHeavyOutput() {
        shopId = 0;
        name = "";
        description = "";
        userId = 0;
        orders = new ArrayList<>();
        products = new ArrayList<>();
    }

    public ShopHeavyOutput(Shop savedShop) {
        this();
        shopId = savedShop.getShopId();
        name = savedShop.getName();
        description = savedShop.getDescription();
        userId = savedShop.getUserId();

        if(savedShop.getOrders() != null) {
            for(Order order: savedShop.getOrders()) {
                orders.add(new OrderHeavyOutput(order));
            }
        }

        if(savedShop.getProducts() != null) {
            for(Product product: savedShop.getProducts()) {
                products.add(new ProductOutput(product));
            }
        }
    }
}
