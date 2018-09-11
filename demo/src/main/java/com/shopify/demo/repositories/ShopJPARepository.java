package com.shopify.demo.repositories;

import com.shopify.demo.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopJPARepository extends JpaRepository<Shop, Integer> {

    Shop findShopByShopId(Integer shopId);
}
