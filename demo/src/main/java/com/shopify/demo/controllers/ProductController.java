package com.shopify.demo.controllers;

import com.shopify.demo.models.LineItem;
import com.shopify.demo.models.Product;
import com.shopify.demo.models.Shop;
import com.shopify.demo.models.iomodels.*;
import com.shopify.demo.repositories.LineItemRepository;
import com.shopify.demo.repositories.ProductRepository;
import com.shopify.demo.repositories.ShopRepository;
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
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    LineItemRepository lineItemRepository;

    /**
     * createProduct: create new Product for the Shop with id, shopId using the fields of newProduct
     * @param newProduct
     * @param shopId
     * @return the newly created Product
     * @throws Exception
     */
    @PostMapping("/shop/{shopId}/product/create/secure")
    private ResponseEntity<ProductOutput> createProduct(@RequestBody ProductInput newProduct, @PathVariable Integer shopId, @RequestHeader String authorization) throws Exception{
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

        Product product = updateProductWithFlag(newProduct, -1,true, shopId);

        if(product == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Create unsuccessful")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Create successful")
                .body(new ProductOutput(product));
    }

    /**
     * getAllProductMin: gets all minimized products.
     *  Use cases: User wants to skim through all products for available purchase
     * @return list of all Products
     */
    @GetMapping("/product/all/min")
    private ResponseEntity<ProductListWrapper> getAllProductMin() {
        List<Product> productList = productRepository.getAll();

        if(productList == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ProductListWrapper());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ProductListWrapper(productList));
    }

    /**
     * getAllProduct: gets all products.
     *  Use cases: User wants to skim through all products for available purchase
     * @return list of all Products
     */
    @GetMapping("/product/all")
    private ResponseEntity<ProductListHeavyWrapper> getAllProduct() {
        List<Product> productList = productRepository.getAll();

        if(productList == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ProductListHeavyWrapper());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ProductListHeavyWrapper(productList));
    }

    /**
     * getLineItemsByProductId: gets list of List Items of Product with id, productId
     * @param productId
     * @return
     */
    @GetMapping("/product/{productId}/line-item/all")
    private ResponseEntity<LineItemListWrapper> getLineItemsByProductId(@PathVariable Integer productId) {
        List<LineItem> lineItems = lineItemRepository.getAllByProductId(productId);

        if(lineItems == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new LineItemListWrapper());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new LineItemListWrapper(lineItems));
    }

    /**
     * getProductByIdMin: gets minimized Product with id, productId
     * @param productId
     * @return the requested Product
     * @throws Exception
     */
    @GetMapping("/product/{productId}/min")
    private ResponseEntity<ProductOutput> getProductByIdMin(@PathVariable Integer productId) throws Exception {
        Product product = productRepository.getProductById(productId);

        if(product == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: No Product exists with this id: " + productId)
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ProductOutput(product));
    }

    /**
     * getProductById: gets Product with id, productId
     * @param productId
     * @return the requested Product
     * @throws Exception
     */
    @GetMapping("/product/{productId}")
    private ResponseEntity<ProductHeavyOutput> getProductById(@PathVariable Integer productId) throws Exception {
        Product product = productRepository.getProductById(productId);

        if(product == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: No Product exists with this id: " + productId)
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ProductHeavyOutput(product));
    }

    /**
     * updateProduct: updates Product with id, productId, using updateProduct fields
     * @param updatedProduct
     * @param productId
     * @return an updated existing Product
     * @throws Exception
     */
    @PutMapping("/product/{productId}/secure")
    private ResponseEntity<ProductHeavyOutput> updateProduct(@RequestBody ProductInput updatedProduct,
                                                             @PathVariable Integer productId, @RequestHeader String authorization) throws Exception {
        // security check to make sure
        try {
            // parse token
            Claims body = Jwts.parser()
                    .setSigningKey("secret")
                    .parseClaimsJws(authorization)
                    .getBody(); // parse then get body of request

            Product product = productRepository.getProductById(productId);

            if(product == null || product.getShop().getUserId() != Integer.parseInt((String)body.get("userId")))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .header("Status", "401: Unauthorized")
                        .body(null);

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Status", "400: Invalid Token")
                    .body(null);
        }
        Product product = updateProductWithFlag(updatedProduct, productId, false, -1);

        if(product == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Shop or Product Id does not exist")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ProductHeavyOutput(product));
    }

    /**
     * updateProductWithFlag: updates existing Product with id, productId, using the updatedProduct fields (and shopId); creates a new Product
     *  if createNewFlag is true
     * @param updatedProduct
     * @param id
     * @param createNewFlag
     * @param shopId
     * @return a newly generated Product or updated existing Product
     * @throws Exception
     */
    private Product updateProductWithFlag(ProductInput updatedProduct, Integer id, boolean createNewFlag, Integer shopId) throws Exception {
        Product prod;

        if(!validProductInput(updatedProduct)) return null;

        // check if new Product is being created
        if(createNewFlag) {
            if(productRepository.getProductByName(updatedProduct.getName()) != null) return null;

            prod = new Product();

            // set shopId
            Shop shop = shopRepository.getShopById(shopId);
            if(shop == null) return null;
            prod.setShopId(shopId);
        } else {
            prod = productRepository.getProductById(id);
        }

        if(prod == null) return null;

        // set new properties
        prod.setName(updatedProduct.getName());
        prod.setDescription(updatedProduct.getDescription());
        prod.setPrice(updatedProduct.getPrice());

        prod = productRepository.saveProduct(prod);

        return prod;
    }

    private boolean validProductInput(ProductInput updatedProduct) {
        return updatedProduct.getName() != null
            && updatedProduct.getName().length() > 0
            && updatedProduct.getPrice() != null
            && updatedProduct.getPrice() > 0;
    }


    /**
     * deleteProduct: deletes Product with id, productId
     * @param productId
     * @return successful deletion message
     */
    @DeleteMapping("/product/{productId}/secure")
    private ResponseEntity<Message> deleteProduct(@PathVariable Integer productId, @RequestHeader String authorization) {

        // security check to make sure
        try {
            // parse token
            Claims body = Jwts.parser()
                    .setSigningKey("secret")
                    .parseClaimsJws(authorization)
                    .getBody(); // parse then get body of request

            Product product = productRepository.getProductById(productId);

            if(product == null || product.getShop().getUserId() != Integer.parseInt((String)body.get("userId")))
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
            productRepository.deleteProductById(productId);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Status", "500: Delete unsuccessful")
                    .body(new Message("Delete unsuccessful"));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Delete successful")
                .body(new Message("Delete successful"));
    }
}
