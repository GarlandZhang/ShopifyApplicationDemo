package com.shopify.demo.controllers;

import com.shopify.demo.models.Order;
import com.shopify.demo.models.Product;
import com.shopify.demo.models.Shop;
import com.shopify.demo.models.User;
import com.shopify.demo.models.iomodels.*;
import com.shopify.demo.repositories.OrderRepository;
import com.shopify.demo.repositories.ProductRepository;
import com.shopify.demo.repositories.ShopRepository;
import com.shopify.demo.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    UserRepository userRepository;

    /**
     * createNew: creates new Shop with newShop fields
     * @param newShop
     * @return newly created Shop
     * @throws Exception
     */
    @PostMapping("/create")
    private ResponseEntity<ShopOutput> createNew(@RequestBody ShopInput newShop) throws Exception {

        Shop savedShop = updateShopByIdWithFlag(newShop, -1, true);

        if(savedShop == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Create unsuccessful")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Create successful")
                .body(new ShopOutput(savedShop));
    }

    /**
     * getAllShops: returns all Shops in database.
     * Use case: Users that want to see all available Shops
     * @return list of all Shops
     */
    @GetMapping("/all")
    private ResponseEntity<ShopListHeavyWrapper> getAllShops() {

        List<Shop> shops = shopRepository.getAll();

        if(shops == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ShopListHeavyWrapper());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ShopListHeavyWrapper(shops));
    }

    /**
     * getAllShops: returns all Shops in database with reduced size.
     * Use case: Users that want to see all available Shops
     * @return list of all Shops
     */
    @GetMapping("/all/min")
    private ResponseEntity<ShopListWrapper> getAllShopsMin() {

        List<Shop> shops = shopRepository.getAll();

        if(shops == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ShopListWrapper());

        ShopListWrapper shopListWrapper = new ShopListWrapper(shops);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ShopListWrapper(shops));
    }

    /**
     * getAllProductInShop: get all minimized Products belonging to this Shop with id, shopId
     * @param shopId
     * @return list of all the Shop's Products
     */
    @GetMapping("/{shopId}/product/all/min")
    private ResponseEntity<ProductListWrapper> getAllProductInShopMin(@PathVariable Integer shopId) {
        List<Product> productList = productRepository.getAllByShopId(shopId);

        if(productList == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ProductListWrapper());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ProductListWrapper(productList));
    }

    /**
     * getAllProductInShop: get all Products belonging to this Shop with id, shopId
     * @param shopId
     * @return list of all the Shop's Products
     */
    @GetMapping("/{shopId}/product/all")
    private ResponseEntity<ProductListHeavyWrapper> getAllProductInShop(@PathVariable Integer shopId) {
        List<Product> productList = productRepository.getAllByShopId(shopId);

        if(productList == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ProductListHeavyWrapper());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ProductListHeavyWrapper(productList));
    }

    /**
     * getAllOrderInShopMin: gets all minimized Orders belonging to this Shop with id, shopId
     * @param shopId
     * @return list of all Shop's Orders
     */
    @GetMapping("/{shopId}/order/all/min")
    private ResponseEntity<OrderListWrapper> getAllOrderInShopMin(@PathVariable Integer shopId) {
        List<Order> orderList = orderRepository.getAllByShopId(shopId);

        if(orderList == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new OrderListWrapper());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new OrderListWrapper(orderList));
    }

    /**
     * getAllOrderInShop: gets all Orders belonging to this Shop with id, shopId
     * @param shopId
     * @return list of all Shop's Orders
     */
    @GetMapping("/{shopId}/order/all")
    private ResponseEntity<OrderListHeavyWrapper> getAllOrderInShop(@PathVariable Integer shopId) {
        List<Order> orderList = orderRepository.getAllByShopId(shopId);

        if(orderList == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new OrderListHeavyWrapper());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new OrderListHeavyWrapper(orderList));
    }

/*    //TODO: belongs in vendor controller but leave here for now
     @GetMapping("/vendor/{userId}")
     private List<Shop> getAllShopsByVendor(@PathVariable Integer userId) {
     List<Shop> products = shopRepository.getAllMinByVendor(userId);

     if(products == null) return new ArrayList<Shop>();

     return products;
     }*/

    /**
     * getShopByIdMin: gets minized Shop with id, shopId
     * @param shopId
     * @return the requested Shop
     */
    @GetMapping("/{shopId}/min")
    private ResponseEntity<ShopOutput> getShopByIdMin(@PathVariable Integer shopId) throws Exception {
        Shop shop = shopRepository.getShopById(shopId);

        if(shop == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: No Shop exists with id: " + shopId)
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ShopOutput(shop));
    }

    /**
     * getShopById: gets Shop with id, shopId
     * @param shopId
     * @return the requested Shop
     */
    @GetMapping("/{shopId}")
    private ResponseEntity<ShopHeavyOutput> getShopById(@PathVariable Integer shopId) throws Exception {
        Shop shop = shopRepository.getShopById(shopId);

        if(shop == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: No Shop exists with id: " + shopId)
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ShopHeavyOutput(shop));
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
    private Shop updateShopByIdWithFlag(@RequestBody ShopInput updatedShop, Integer id, boolean createNewFlag) throws Exception {
        Shop shop = null;

        if(!validShopInput(updatedShop)) return null;

        Shop existingShop = shopRepository.getShopByName(updatedShop.getName());

        if(createNewFlag) {
            if(existingShop != null) return null;
            shop = new Shop();
        } else {
            if(existingShop != null
            && existingShop.getShopId() != id) return null;
            shop = shopRepository.getShopById(id);
        }

        if(shop == null) return null;

        // update with new properties
        shop.setName(updatedShop.getName());
//        shop.setUserId(updatedShop.getUserId());
        shop.setDescription(updatedShop.getDescription());

        shop = shopRepository.saveShop(shop);

        return shop;
    }

    private boolean validShopInput(ShopInput updatedShop) {
        return updatedShop.getName() != null
                && updatedShop.getName().length() > 0;
//                && updatedShop.getUserId() != null;
    }

    /**
     * updateShopById: updates Shop with id, shopId, using updatedShop fields
     * @param updatedShop
     * @param shopId
     * @return updated Shop
     * @throws Exception
     */
    @PutMapping("/{shopId}/secure")
    private ResponseEntity<ShopHeavyOutput> updateShopById(@RequestBody ShopInput updatedShop, @PathVariable Integer shopId, @RequestHeader String authorization) throws Exception {

        // security check to make sure
        try {
            // parse token
            Claims body = Jwts.parser()
                    .setSigningKey("secret")
                    .parseClaimsJws(authorization)
                    .getBody(); // parse then get body of request

            Shop shop = shopRepository.getShopById(shopId);

            if(shop == null || shop.getUserId() != Integer.parseInt((String)body.get("userId")))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .header("Status", "401: Unauthorized")
                        .body(null);

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Status", "400: Invalid Token")
                    .body(null);
        }

        Shop shop = updateShopByIdWithFlag(updatedShop, shopId, false);

        if(shop == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Shop does not exist with id: " + shopId)
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Update successful")
                .body(new ShopHeavyOutput(shop));
    }

    /**
     * deleteShopById: deletes Shop with id, shopId
     * @param shopId
     * @return successful deletion message
     * @throws Exception
     */
    @DeleteMapping("/{shopId}/secure")
    private ResponseEntity<Message> deleteShopById(@PathVariable Integer shopId, @RequestHeader String authorization) throws Exception {

        // security check to make sure
        try {
            // parse token
            Claims body = Jwts.parser()
                    .setSigningKey("secret")
                    .parseClaimsJws(authorization)
                    .getBody(); // parse then get body of request

            Shop shop = shopRepository.getShopById(shopId);

            if(shop == null || shop.getUserId() != Integer.parseInt((String)body.get("userId")))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .header("Status", "401: Unauthorized")
                        .body(null);

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Status", "400: Invalid Token")
                    .body(null);
        }

        try{
            shopRepository.deleteShopById(shopId);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Status", "500: Delete unsuccessful")
                    .body(new Message("Delete unsuccessful"));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Deletion successful")
                .body(new Message("Delete successful"));
    }

}
