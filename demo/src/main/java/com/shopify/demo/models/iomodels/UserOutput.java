package com.shopify.demo.models.iomodels;

import com.shopify.demo.models.Order;
import com.shopify.demo.models.Shop;
import com.shopify.demo.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserOutput {

    Integer userId;
    String username;
    String role;
    List<Integer> shops;
    List<Integer> orders;

    public UserOutput() {
        userId = 0;
        username = "";
        role = "";
        shops = new ArrayList<>();
        orders = new ArrayList<>();
    }

    public UserOutput(User user) {
        userId = user.getUserId();
        username = user.getUsername();
        role = user.getRole();

        if(user.getShops() != null) {
            for(Shop shop: user.getShops()) {
                shops.add(shop.getShopId());
            }
        }

        if(user.getOrders() != null) {
            for(Order order: user.getOrders()) {
                orders.add(order.getOrderId());
            }
        }
    }
}
