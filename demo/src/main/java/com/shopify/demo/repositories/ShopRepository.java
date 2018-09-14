package com.shopify.demo.repositories;

import com.shopify.demo.models.Shop;

import java.util.List;

public interface ShopRepository {

    void saveShop(Shop shop);

    Shop getShopById(Integer shopId);

    Shop getShopByIdMin(Integer shopId);

    void deleteShopById(Integer shopId);

    List<Shop> findAll();

    List<Shop> getAllMin();

    List<Shop> getAllByVendor(Integer vendorId);

    List<Shop> getAllMinByVendor(Integer vendorId);
}
