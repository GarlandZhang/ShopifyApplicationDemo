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
    private Product createProduct(@RequestBody Product newProduct, @PathVariable Integer shopId) throws Exception{
        return updateProductWithFlag(newProduct, -1,true, shopId);
    }

    /**
     * getAllProduct: gets all products.
     *  Use cases: User wants to skim through all products for available purchase
     * @return list of all Products
     */
    @GetMapping("/product/all")
    private List<Product> getAllProduct() {
        List<Product> productList = productRepository.getAllMin();

        if(productList == null) return new ArrayList<>();

        return productList;
    }

    /**
     * getLineItemsByProductId: gets list of List Items of Product with id, productId
     * @param productId
     * @return
     */
    @GetMapping("/product/{productId}/line-item/all")
    private List<LineItem> getLineItemsByProductId(@PathVariable Integer productId) {
        List<LineItem> lineItems = lineItemRepository.getAllByProductIdMin(productId);

        if(lineItems == null) return new ArrayList<>();

        return lineItems;
    }

    /**
     * getProductById: gets Product with id, productId
     * @param productId
     * @return the requested Product
     * @throws Exception
     */
    @GetMapping("/product/{productId}")
    private Product getProductById(@PathVariable Integer productId) throws Exception {
        Product product = productRepository.getProductByIdMin(productId);

        if(product == null) throw new Exception("500: Product does not exist");

        return product;
    }

    /**
     * updateProduct: updates Product with id, productId, using updateProduct fields
     * @param updatedProduct
     * @param productId
     * @return an updated existing Product
     * @throws Exception
     */
    @PutMapping("/product/{productId}")
    private Product updateProduct(@RequestBody Product updatedProduct,
                                  @PathVariable Integer productId) throws Exception {
        return updateProductWithFlag(updatedProduct, productId, false, -1);
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
            Shop shop = shopRepository.getShopByIdMin(shopId);
            if(shop == null) throw new Exception("500: Shop by this id does not exist");
            prod.setShopId(shopId);
        } else {
            prod = productRepository.getProductByIdMin(id);
        }

        if(prod == null) throw new Exception("500: Product by this id does not exist");

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
    private String deleteProduct(@PathVariable Integer productId) {
        productRepository.deleteProductById(productId);

        return "200: deleted successful";
    }
}
