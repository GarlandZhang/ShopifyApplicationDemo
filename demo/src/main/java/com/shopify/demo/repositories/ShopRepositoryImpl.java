package com.shopify.demo.repositories;

import com.shopify.demo.models.Shop;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
