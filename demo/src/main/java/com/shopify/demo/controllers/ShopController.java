package com.shopify.demo.controllers;

import com.shopify.demo.models.Order;
import com.shopify.demo.models.Product;
import com.shopify.demo.models.Shop;
import com.shopify.demo.repositories.OrderRepository;
import com.shopify.demo.repositories.ProductRepository;
import com.shopify.demo.repositories.ShopRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @PostMapping("/create")
    private Shop createNew(@RequestBody Shop newShop, boolean createNewFlag) throws Exception {

        Shop savedShop = updateShopByIdWithFlag(newShop, -1, true);

        if(savedShop == null) throw new Exception("500: Could not successfully save file");

        return savedShop;
    }


    @GetMapping("/all")
    private List<Shop> getAllShops() {

        List<Shop> shops = shopRepository.getAllMin();

        if(shops == null) return new ArrayList<Shop>();

        return shops;
    }

    @GetMapping("/{shopId}/product/all")
    private List<Product> getAllProductInShop(@PathVariable Integer shopId) {
        List<Product> productList = productRepository.getAllByShopIdMin(shopId);

        if(productList == null) return new ArrayList<>();

        return productList;
    }

    @GetMapping("/{shopId}/order/all")
    private List<Order> getAllOrderInShop(@PathVariable Integer shopId) {
        List<Order> orderList = orderRepository.getAllByShopIdMin(shopId);

        if(orderList == null) return new ArrayList<>();

        return orderList;
    }

/*    //TODO: belongs in vendor controller but leave here for now
    @GetMapping("/vendor/{vendorId}")
    private List<Shop> getAllShopsByVendor(@PathVariable Integer vendorId) {
        List<Shop> shops = shopRepository.getAllMinByVendor(vendorId);

        if(shops == null) return new ArrayList<Shop>();

        return shops;
    }*/

    @GetMapping("/{shopId}")
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

        if(shop == null) throw new Exception("500: shop does not exist with this id");

        shop.setName(updatedShop.getName());
        shop.setVendorId(updatedShop.getVendorId());
        shop.setDescription(updatedShop.getDescription());

        shopRepository.saveShop(shop);

        return shop;
    }

    @PutMapping("/{shopId}")
    private Shop updateShopById(@RequestBody Shop updatedShop, @PathVariable Integer shopId) throws Exception {
        Shop shop = updateShopByIdWithFlag(updatedShop, shopId, false);

        if(shop == null) throw new Exception("500: shop does not exist");

        return shop;
    }

    @DeleteMapping("/{shopId}")
    private String deleteShopById(@PathVariable Integer shopId) throws Exception {
        shopRepository.deleteShopById(shopId);
        return "200: successfully deleted";
    }

}
