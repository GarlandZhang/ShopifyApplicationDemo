package com.shopify.demo.repositories;

import com.shopify.demo.models.Shop;

import java.util.List;

public interface ShopRepository {

    Shop saveShop(Shop shop);

    Shop getShopById(Integer shopId);

    Shop getShopByIdAndMinify(Integer shopId);

    void deleteShopById(Integer shopId);

    List<Shop> getAll();

    List<Shop> getAllAndMinify();

    List<Shop> getAllByUser(Integer userId);

    List<Shop> getAllAndMinifyByVendor(Integer vendorId);

    Shop getShopByName(String name);
}
