package com.shopify.demo.models.iomodels;

import com.shopify.demo.models.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductListHeavyWrapper {
    List<ProductHeavyOutput> products;

    public ProductListHeavyWrapper() {
        products = new ArrayList<>();
    }

    public ProductListHeavyWrapper(List<Product> products) {
        this();
        if(products != null) {
            for(Product product : products) {
                this.products.add(new ProductHeavyOutput(product));
            }
        }
    }
}
