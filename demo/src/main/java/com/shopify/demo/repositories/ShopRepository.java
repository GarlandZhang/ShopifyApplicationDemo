package com.shopify.demo.repositories;

import com.shopify.demo.models.Shop;

public interface ShopRepository {

    void saveShop(Shop shop);

    Shop getShopById(Integer shopId);
}
