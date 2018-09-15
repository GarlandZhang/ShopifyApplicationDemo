package com.shopify.demo.repositories;

import com.shopify.demo.models.Product;

import java.util.List;

public interface ProductRepository {
    Product getProductById(Integer id);

    Product getProductByIdAndMinify(Integer id);

    void save(Product prod);

    void deleteProductById(Integer productId);

    List<Product> getAll();

    List<Product> getAllAndMinify();

    List<Product> getAllByShopId(Integer shopId);

    List<Product> getAllByShopIdAndMinify(Integer shopId);

    Product getProductByLineItemId(Integer lineItemId);

    Product getProductByLineItemIdAndMinify(Integer lineItemId);
}
