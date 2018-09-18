package com.shopify.demo.repositories;

import com.shopify.demo.models.Order;
import com.shopify.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductJPARepository extends JpaRepository<Product, Integer> {

    Product findProductByProductId(Integer id);

    List<Product> findAllByShopId(Integer shopId);

    @Query(value = "SELECT p, l from Product, LineItem where (p.productId = l.productId AND l.lineItemId = :id)", nativeQuery = true)
    Product getProductByGivenLineItemId(@Param("id") Integer lineItemId);

    Product findProductByName(String name);
}
