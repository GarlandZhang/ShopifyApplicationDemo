package com.shopify.demo.repositories;

import com.shopify.demo.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ShopJPARepository extends JpaRepository<Shop, Integer> {

    Shop findShopByShopId(Integer shopId);

    List<Shop> findAll();

    List<Shop> findAllByVendorId(Integer vendorId);

    Shop findShopByName(String name);
}
