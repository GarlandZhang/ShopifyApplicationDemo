package com.shopify.demo.repositories;

import com.shopify.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductJPARepository extends JpaRepository<Product, Integer> {

    Product findProductByProductId(Integer id);

    List<Product> findAllByShopIdVal(Integer shopId);
}
