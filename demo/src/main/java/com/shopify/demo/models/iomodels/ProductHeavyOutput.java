package com.shopify.demo.models.iomodels;

import com.shopify.demo.models.LineItem;
import com.shopify.demo.models.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ProductHeavyOutput {

    Integer productId;
    String description;
    Float price;
    Integer shopId;
    List<LineItemOutput> lineItems;

    public ProductHeavyOutput(){
        productId = 0;
        description = "";
        price = (float) 0;
        shopId = 0;
        lineItems = new ArrayList<>();
    }

    public ProductHeavyOutput(Product product) {
        this();
        productId = product.getProductId();
        description = product.getDescription();
        price = product.getPrice();
        shopId = product.getShopId();

        if(product.getLineItems() != null) {
            for(LineItem lineItem : product.getLineItems()) {
                lineItems.add(new LineItemOutput(lineItem));
            }
        }
    }
}
