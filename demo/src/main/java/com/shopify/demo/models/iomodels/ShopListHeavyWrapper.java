package com.shopify.demo.models.iomodels;

import com.shopify.demo.models.Shop;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ShopListHeavyWrapper {
    private List<ShopHeavyOutput> shops;

    public ShopListHeavyWrapper() {
        shops = new ArrayList<>();
    }

    public ShopListHeavyWrapper(List<Shop> shops) {
        this();
        if(shops != null) {
            for(Shop shop : shops) {
                this.shops.add(new ShopHeavyOutput(shop));
            }
        }
    }
}
