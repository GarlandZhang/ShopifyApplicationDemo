package com.shopify.demo.controllers;

import com.shopify.demo.models.Shop;
import com.shopify.demo.repositories.ShopRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/shop")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
public class ShopController {

    @Autowired
    ShopRepository shopRepository;

    /*
    TODO: clean up how data is being retrieved (should not be via params),
    TODO: fix up return type
     */
    @PostMapping("create/new")
    private String createNew(@RequestParam("name") String name,
                             @RequestParam("vendorId") Integer vendorId,
                             @RequestParam("description") String description) {
        Shop shop = new Shop();
        shop.setName(name);
        shop.setVendorId(vendorId);
        shop.setDescription(description);

        shopRepository.saveShop(shop);

        return "created";
    }

    @GetMapping("get/id")
    private Shop getShopById(@RequestParam("shopId") Integer shopId) {
        Shop shop = shopRepository.getShopById(shopId);
        shop.setOrders(null); //TODO: find better way to handle this
        shop.setProducts(null);

        return shop;
    }
}
