package com.shopify.demo.controllers;

import com.shopify.demo.models.Shop;
import com.shopify.demo.repositories.ShopRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/shop")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
public class ShopController {

    @Autowired
    ShopRepository shopRepository;

    @PostMapping("/create")
    private Shop createNew(@RequestBody Shop newShop, boolean createNewFlag) throws Exception {

        Shop savedShop = updateShopByIdWithFlag(newShop, -1, true);

        if(savedShop == null) throw new Exception("Could not successfully save file");

        return savedShop;
    }

    @GetMapping("/all")
    private List<Shop> getAllShops() {

        List<Shop> shops = shopRepository.getAllMin();

        if(shops == null) return new ArrayList<Shop>();

        return shops;
    }

    @GetMapping("/vendor/{vendorId}")
    private List<Shop> getAllShopsByVendor(@PathVariable Integer vendorId) {
        List<Shop> shops = shopRepository.getAllMinByVendor(vendorId);

        if(shops == null) return new ArrayList<Shop>();

        return shops;
    }

    @GetMapping("/id/{shopId}")
    private Shop getShopById(@PathVariable Integer shopId) {
        return shopRepository.getShopByIdMin(shopId);
    }

    private Shop updateShopByIdWithFlag(@RequestBody Shop updatedShop, Integer id, boolean createNewFlag) throws Exception {
        Shop shop = null;
        if(createNewFlag) {
            shop = new Shop();
        } else {
            shop = shopRepository.getShopByIdMin(id);
        }

        if(shop == null) throw new Exception("shop does not exist with this id");

        shop.setName(updatedShop.getName());
        shop.setVendorId(updatedShop.getVendorId());
        shop.setDescription(updatedShop.getDescription());

        shopRepository.saveShop(shop);

        return shop;
    }

    @PutMapping("/update/id/{shopId}")
    private Shop updateShopById(@RequestBody Shop updatedShop, @PathVariable Integer shopId) throws Exception {
        Shop shop = updateShopByIdWithFlag(updatedShop, shopId, false);

        if(shop == null) throw new Exception("shop does not exist");

        return shop;
    }

    @DeleteMapping("/delete/id/{shopId}")
    private String deleteShopById(@RequestBody Shop shopToDelete, @PathVariable Integer shopId) throws Exception {
        shopRepository.deleteShopById(shopId);
        return "deleted";
    }

}
