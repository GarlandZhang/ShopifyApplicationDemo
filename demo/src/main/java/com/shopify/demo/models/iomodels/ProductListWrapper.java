package com.shopify.demo.models.iomodels;

import com.shopify.demo.models.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductListWrapper {
    List<ProductOutput> products;

    public ProductListWrapper() {
        products = new ArrayList<>();
    }

    public ProductListWrapper(List<Product> products) {
        this();
        if(products != null) {
            for(Product product : products) {
                this.products.add(new ProductOutput(product));
            }
        }
    }
}
