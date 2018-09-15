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

    @PostMapping("/shop/{shopId}/product/create")
    private Product createProduct(@RequestBody Product newProduct, @PathVariable Integer shopId) throws Exception{
        return updateProductWithFlag(newProduct, -1,true, shopId);
    }

    @GetMapping("/product/all")
    private List<Product> getAllProduct() {
        List<Product> productList = productRepository.getAllMin();

        if(productList == null) return new ArrayList<>();

        return productList;
    }

    @GetMapping("/product/{productId}/line-item/all")
    private List<LineItem> getLineItemsByProductId(@PathVariable Integer productId) {
        List<LineItem> lineItems = lineItemRepository.getAllByProductIdMin(productId);

        if(lineItems == null) return new ArrayList<>();

        return lineItems;
    }

    @GetMapping("/product/{productId}")
    private Product getProductById(@PathVariable Integer productId) throws Exception {
        Product product = productRepository.getProductByIdMin(productId);

        if(product == null) throw new Exception("500: Product does not exist");

        return product;
    }

    @PutMapping("/product/{productId}")
    private Product updateProduct(@RequestBody Product updatedProduct,
                                  @PathVariable Integer productId) throws Exception {
        return updateProductWithFlag(updatedProduct, productId, false, -1);
    }

    private Product updateProductWithFlag(Product newProduct, Integer id, boolean createNewFlag, Integer shopId) throws Exception {
        Product prod;

        if(createNewFlag) {
            prod = new Product();
            Shop shop = shopRepository.getShopByIdMin(shopId);
            if(shop != null){
                prod.setShopId(shopId);
//                prod.setShop(shop);
            } else throw new Exception("500: Shop by this id does not exist");
        } else {
            prod = productRepository.getProductByIdMin(id);
        }

        if(prod == null) throw new Exception("500: Product by this id does not exist");

        prod.setName(newProduct.getName());
        prod.setDescription(newProduct.getDescription());
        prod.setPrice(newProduct.getPrice());

        productRepository.save(prod);

        return prod;
    }


    @DeleteMapping("/product/{productId}")
    private String deleteProduct(@PathVariable Integer productId) {
        productRepository.deleteProductById(productId);

        return "200: deleted successful";
    }
}
