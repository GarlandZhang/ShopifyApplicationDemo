package com.shopify.demo.repositories;

import com.shopify.demo.models.Product;

import java.util.List;

public interface ProductRepository {
    Product getProductById(Integer id);

    Product getProductByIdMin(Integer id);

    void save(Product prod);

    void deleteProductById(Integer productId);

    List<Product> getAll();

    List<Product> getAllMin();

    List<Product> getAllByShopId(Integer shopId);

    List<Product> getAllByShopIdMin(Integer shopId);

    Product getProductByLineItemId(Integer lineItemId);

    Product getProductByLineItemIdMin(Integer lineItemId);
}
