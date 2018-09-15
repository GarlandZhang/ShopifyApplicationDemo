package com.shopify.demo.controllers;

import com.shopify.demo.models.LineItem;
import com.shopify.demo.models.Product;
import com.shopify.demo.models.Shop;
import com.shopify.demo.repositories.LineItemRepository;
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
    @PostMapping("/shop/{shopId}/product/create")
    private ResponseEntity<Product> createProduct(@RequestBody Product newProduct, @PathVariable Integer shopId) throws Exception{
        Product product = updateProductWithFlag(newProduct, -1,true, shopId);

        if(product == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Status", "500: Save unsuccessful")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Save successful")
                .body(product);
    }

    /**
     * getAllProduct: gets all products.
     *  Use cases: User wants to skim through all products for available purchase
     * @return list of all Products
     */
    @GetMapping("/product/all")
    private ResponseEntity<List<Product>> getAllProduct() {
        List<Product> productList = productRepository.getAllAndMinify();

        if(productList == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ArrayList());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(productList);
    }

    /**
     * getLineItemsByProductId: gets list of List Items of Product with id, productId
     * @param productId
     * @return
     */
    @GetMapping("/product/{productId}/line-item/all")
    private ResponseEntity<List<LineItem>> getLineItemsByProductId(@PathVariable Integer productId) {
        List<LineItem> lineItems = lineItemRepository.getAllByProductIdAndMinify(productId);

        if(lineItems == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ArrayList<>());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(lineItems);
    }

    /**
     * getProductById: gets Product with id, productId
     * @param productId
     * @return the requested Product
     * @throws Exception
     */
    @GetMapping("/product/{productId}")
    private ResponseEntity<Product> getProductById(@PathVariable Integer productId) throws Exception {
        Product product = productRepository.getProductByIdAndMinify(productId);

        if(product == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Status", "500: No Product exists with this id: " + productId)
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(product);
    }

    /**
     * updateProduct: updates Product with id, productId, using updateProduct fields
     * @param updatedProduct
     * @param productId
     * @return an updated existing Product
     * @throws Exception
     */
    @PutMapping("/product/{productId}")
    private ResponseEntity<Product> updateProduct(@RequestBody Product updatedProduct,
                                  @PathVariable Integer productId) throws Exception {
        Product product = updateProductWithFlag(updatedProduct, productId, false, -1);

        if(product == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "500: Shop or Product Id does not exist")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(product);
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
    private Product updateProductWithFlag(Product updatedProduct, Integer id, boolean createNewFlag, Integer shopId) throws Exception {
        Product prod;

        // check if new Product is being created
        if(createNewFlag) {
            prod = new Product();

            // set shopId
            Shop shop = shopRepository.getShopByIdAndMinify(shopId);
            if(shop == null) return null;
            prod.setShopId(shopId);
        } else {
            prod = productRepository.getProductByIdAndMinify(id);
        }

        if(prod == null) return null;

        // set new properties
        prod.setName(updatedProduct.getName());
        prod.setDescription(updatedProduct.getDescription());
        prod.setPrice(updatedProduct.getPrice());

        productRepository.save(prod);

        return prod;
    }


    /**
     * deleteProduct: deletes Product with id, productId
     * @param productId
     * @return successful deletion message
     */
    @DeleteMapping("/product/{productId}")
    private ResponseEntity<String> deleteProduct(@PathVariable Integer productId) {
        productRepository.deleteProductById(productId);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Delete successful")
                .body("Delete successful");
    }
}
