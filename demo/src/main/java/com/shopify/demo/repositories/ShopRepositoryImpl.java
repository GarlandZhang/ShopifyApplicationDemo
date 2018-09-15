package com.shopify.demo.repositories;

import com.shopify.demo.models.Shop;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    public Shop getShopByIdAndMinify(Integer shopId) {
        return minifify(getShopById(shopId));
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
    public List<Shop> getAllAndMinify() {
        List<Shop> shops =  findAll();

        for(Shop shop: shops) {
            minifify(shop);
        }

        return shops;
    }

    @Override
    public List<Shop> getAllAndMinifyByVendor(Integer vendorId) {
        List<Shop> shops = getAllByVendor(vendorId);

        for(Shop shop: shops) {
            minifify(shop);
        }

        return shops;
    }

    @Override
    public List<Shop> getAllByVendor(Integer vendorId) {
        return shopJPARepository.findAllByVendorId(vendorId);
    }

    /**
     * minify: reduces size of Shop object to reduce fetch load; prevents fetching for parent and children objects
     * @param shop
     * @return minified Shop
     */
    private Shop minifify(Shop shop) {
        if(shop != null) {
            shop.setProducts(new ArrayList<>());
            shop.setOrders(new ArrayList<>());
            return shop;
        }

        return shop;
    }
}
