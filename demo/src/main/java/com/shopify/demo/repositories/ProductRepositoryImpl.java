package com.shopify.demo.repositories;

import com.shopify.demo.models.Product;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@NoArgsConstructor
public class ProductRepositoryImpl implements ProductRepository{

    @Autowired
    ProductJPARepository productJPARepository;

    @Autowired
    ShopJPARepository shopJPARepository;

    @Autowired
    LineItemRepository lineItemRepository;

    @Override
    public Product getProductById(Integer id) {
        return productJPARepository.findProductByProductId(id);
    }

    @Override
    public Product getProductByIdMin(Integer id) {
       return minifyProduct(getProductById(id));
    }

    private Product minifyProduct(Product productById) {
        if(productById != null){
            productById.setShop(null);
            productById.setLineItems(new ArrayList<>());
        }

        return productById;
    }

    @Override
    public void save(Product prod) {
        productJPARepository.save(prod);
    }

    @Override
    public void deleteProductById(Integer productId) {
        productJPARepository.deleteById(productId);
    }

    @Override
    public List<Product> getAll() {
        return productJPARepository.findAll();
    }

    @Override
    public List<Product> getAllMin() {
        List<Product> products = getAll();

        if(products != null) {
            for(Product product : products) {
                minifyProduct(product);
            }
        }

        return products;
    }

    @Override
    public List<Product> getAllByShopId(Integer shopId) {
        return productJPARepository.findAllByShopId(shopId);
    }

    @Override
    public List<Product> getAllByShopIdMin(Integer shopId) {
        List<Product> products = getAllByShopId(shopId);

        if(products != null) {
            for(Product product : products) {
                minifyProduct(product);
            }
        }

        return products;
    }

    @Override
    public Product getProductByLineItemId(Integer lineItemId) {
        return productJPARepository.findProductByProductId(lineItemRepository.getLineItemById(lineItemId).getProductId());
    }

    @Override
    public Product getProductByLineItemIdMin(Integer lineItemId) {
        return minifyProduct(getProductByLineItemId(lineItemId));
    }
}
