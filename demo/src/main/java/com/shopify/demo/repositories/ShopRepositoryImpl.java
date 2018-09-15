package com.shopify.demo.repositories;

import com.shopify.demo.models.Shop;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@NoArgsConstructor
public class ShopRepositoryImpl implements ShopRepository{

    @Autowired
    ShopJPARepository shopJPARepository;

    @Override
    public void saveShop(Shop shop) {
        shopJPARepository.save(shop);
    }

    @Override
    public Shop getShopById(Integer shopId) {
        return shopJPARepository.findShopByShopId(shopId);
    }

    @Override
    public Shop getShopByIdMin(Integer shopId) {
        return minififyShop(getShopById(shopId));
    }

    @Override
    public void deleteShopById(Integer shopId) {
        shopJPARepository.deleteById(shopId);
    }

    @Override
    public List<Shop> findAll() {
        return shopJPARepository.findAll();
    }

    @Override
    public List<Shop> getAllMin() {
        List<Shop> shops =  findAll();

        for(Shop shop: shops) {
            minififyShop(shop);
        }

        return shops;
    }

    @Override
    public List<Shop> getAllMinByVendor(Integer vendorId) {
        List<Shop> shops = getAllByVendor(vendorId);

        for(Shop shop: shops) {
            minififyShop(shop);
        }

        return shops;
    }

    @Override
    public List<Shop> getAllByVendor(Integer vendorId) {
        return shopJPARepository.findAllByVendorId(vendorId);
    }

    private Shop minififyShop(Shop shop) {
        if(shop != null) {
            shop.setProducts(null);
//            shop.setOrders(null);
            return shop;
        }

        return shop;
    }
}
