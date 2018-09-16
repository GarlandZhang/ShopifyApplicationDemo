package com.shopify.demo.controllers;

import com.shopify.demo.models.Order;
import com.shopify.demo.models.Product;
import com.shopify.demo.models.Shop;
import com.shopify.demo.repositories.OrderRepository;
import com.shopify.demo.repositories.ProductRepository;
import com.shopify.demo.repositories.ShopRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    /**
     * createNew: creates new Shop with newShop fields
     * @param newShop
     * @return newly created Shop
     * @throws Exception
     */
    @PostMapping("/create")
    private ResponseEntity<Shop> createNew(@RequestBody Shop newShop) throws Exception {

        Shop savedShop = updateShopByIdWithFlag(newShop, -1, true);

        if(savedShop == null) ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Create unsuccessful")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Create successful")
                .body(savedShop);
    }

    /**
     * getAllShops: returns all Shops in database.
     * Use case: Users that want to see all available Shops
     * @return list of all Shops
     */
    @GetMapping("/all")
    private ResponseEntity<List<Shop>> getAllShops() {

        List<Shop> shops = shopRepository.getAllAndMinify();

        if(shops == null) ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "200: Success")
                .body(new ArrayList<>());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(shops);
    }

    @GetMapping("/test")
    private ResponseEntity<List<Shop>> getShops(){
        List<Shop> shops = shopRepository.getAllAndMinify();

        if(Math.random() * 2 > 1) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .header("Status", "400: No shops")
                                    .body(new ArrayList<>());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(shops);
    }

    /**
     * getAllProductInShop: get all Products belonging to this Shop with id, shopId
     * @param shopId
     * @return list of all the Shop's Products
     */
    @GetMapping("/{shopId}/product/all")
    private ResponseEntity<List<Product>> getAllProductInShop(@PathVariable Integer shopId) {
        List<Product> productList = productRepository.getAllByShopIdAndMinify(shopId);

        if(productList == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ArrayList<>());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(productList);
    }

    /**
     * getAllOrderInShop: gets all Orders belonging to this Shop with id, shopId
     * @param shopId
     * @return list of all Shop's Orders
     */
    @GetMapping("/{shopId}/order/all")
    private ResponseEntity<List<Order>> getAllOrderInShop(@PathVariable Integer shopId) {
        List<Order> orderList = orderRepository.getAllByShopIdAndMinify(shopId);

        if(orderList == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ArrayList<>());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(orderList);
    }

/*    //TODO: belongs in vendor controller but leave here for now
    @GetMapping("/vendor/{vendorId}")
    private List<Shop> getAllShopsByVendor(@PathVariable Integer vendorId) {
        List<Shop> shops = shopRepository.getAllMinByVendor(vendorId);

        if(shops == null) return new ArrayList<Shop>();

        return shops;
    }*/

    /**
     * getShopById: gets Shop with id, shopId
     * @param shopId
     * @return the requested Shop
     */
    @GetMapping("/{shopId}")
    private ResponseEntity<Shop> getShopById(@PathVariable Integer shopId) throws Exception {
        Shop shop = shopRepository.getShopByIdAndMinify(shopId);

        if(shop == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: No Shop exists with id: " + shopId)
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(shop);
    }

    /**
     * updateShopByIdWithFlag: updates existing Shop with id using updatedShop fields;
     *  creates new Shop otherwise if createNewFlag is true
     * @param updatedShop
     * @param id
     * @param createNewFlag
     * @return the newly generated Shop or the updated existing Shop
     * @throws Exception
     */
    private Shop updateShopByIdWithFlag(@RequestBody Shop updatedShop, Integer id, boolean createNewFlag) throws Exception {
        Shop shop = null;

        if(createNewFlag) {
            shop = new Shop();
        } else {
            shop = shopRepository.getShopByIdAndMinify(id);
        }

        if(shop == null) return null;

        // update with new properties
        shop.setName(updatedShop.getName());
        shop.setVendorId(updatedShop.getVendorId());
        shop.setDescription(updatedShop.getDescription());

        shopRepository.saveShop(shop);

        return shop;
    }

    /**
     * updateShopById: updates Shop with id, shopId, using updatedShop fields
     * @param updatedShop
     * @param shopId
     * @return updated Shop
     * @throws Exception
     */
    @PutMapping("/{shopId}")
    private ResponseEntity<Shop> updateShopById(@RequestBody Shop updatedShop, @PathVariable Integer shopId) throws Exception {
        Shop shop = updateShopByIdWithFlag(updatedShop, shopId, false);

        if(shop == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Shop does not exist with id: " + shopId)
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Update successful")
                .body(shop);
    }

    /**
     * deleteShopById: deletes Shop with id, shopId
     * @param shopId
     * @return successful deletion message
     * @throws Exception
     */
    @DeleteMapping("/{shopId}")
    private ResponseEntity<String> deleteShopById(@PathVariable Integer shopId) throws Exception {
        shopRepository.deleteShopById(shopId);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Deletion successful")
                .body("Delete sueccessful");
    }

}
