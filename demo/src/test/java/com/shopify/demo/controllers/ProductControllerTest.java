/*
package com.shopify.demo.controllers;

import com.shopify.demo.models.Product;
import com.shopify.demo.models.iomodels.*;
import com.shopify.demo.repositories.ProductRepository;
import com.shopify.demo.repositories.ShopRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProductRepository productRepository;


    @Test
    public void getCreatedProductOutput() throws Exception {
        ProductInput productInput = new ProductInput();
        productInput.setName("Deck Of Cards");
        productInput.setDescription("cards for playing");
        productInput.setPrice((float)6);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductInput> entity = new HttpEntity<ProductInput>(productInput, headers);
        ResponseEntity<ProductOutput> shopOutputResponseEntity = testRestTemplate.postForEntity("/shop/1/product/create", productInput, ProductOutput.class);

        ProductOutput productOutput = shopOutputResponseEntity.getBody();

        assertEquals("Deck Of Cards", productOutput.getName());

        productRepository.deleteProductById(productOutput.getProductId());
    }

    @Test
    public void getCreatedNoNameProductOutput() throws Exception {
        ProductInput productInput = new ProductInput();
        productInput.setName("");
        productInput.setDescription("cards for playing");
        productInput.setPrice((float)6);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ProductOutput> productOutputResponseEntity = testRestTemplate.postForEntity("/shop/1/product/create", productInput, ProductOutput.class);

        assertEquals(BAD_REQUEST, productOutputResponseEntity.getStatusCode());
    }

    @Test
    public void getCreatedDuplicateNameProductOutput() throws Exception {
        ProductInput productInput = new ProductInput();
        productInput.setName("A");
        productInput.setDescription("cards for playing");
        productInput.setPrice((float)6);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ProductOutput> productOutputResponseEntity = testRestTemplate.postForEntity("/shop/1/product/create", productInput, ProductOutput.class);

        ProductInput productInput2 = new ProductInput();
        productInput2.setName("A");
        productInput2.setDescription("cards for playing");
        productInput2.setPrice((float)6);

        ResponseEntity<ProductOutput> productOutputResponseEntity2 = testRestTemplate.postForEntity("/shop/1/product/create", productInput2, ProductOutput.class);

        assertEquals(BAD_REQUEST, productOutputResponseEntity2.getStatusCode());
    }



    @Test
    public void getCreatedProductWithFakeShopOutput() throws Exception {
        ProductInput productInput = new ProductInput();
        productInput.setName("House of cards");
        productInput.setDescription("cards for playing");
        productInput.setPrice((float)6);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ProductOutput> productOutputResponseEntity = testRestTemplate.postForEntity("/shop/-1/product/create", productInput, ProductOutput.class);

        assertEquals(BAD_REQUEST, productOutputResponseEntity.getStatusCode());
    }

    @Test
    public void getProductById() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ProductHeavyOutput> productHeavyOutputResponseEntity = testRestTemplate.getForEntity("/product/1", ProductHeavyOutput.class);

        assertEquals("Steel Playing Cards" , productHeavyOutputResponseEntity.getBody().getName());
    }

    @Test
    public void updateProductWithNoName() throws Exception {
        ProductInput productInput = new ProductInput();
        productInput.setName("");
        productInput.setDescription("cards for playing");
        productInput.setPrice((float)6);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductInput> entity = new HttpEntity<ProductInput>(productInput, headers);
        ResponseEntity<ProductOutput> productOutputResponseEntity = testRestTemplate.exchange("/product/1", PUT, entity, ProductOutput.class);

        assertEquals(HttpStatus.BAD_REQUEST, productOutputResponseEntity.getStatusCode());
    }

    @Test
    public void updateFakeProduct() throws Exception {
        ProductInput productInput = new ProductInput();
        productInput.setName("hjkhjk");
        productInput.setDescription("cards for playing");
        productInput.setPrice((float)6);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductInput> entity = new HttpEntity<ProductInput>(productInput, headers);
        ResponseEntity<ProductOutput> productOutputResponseEntity = testRestTemplate.exchange("/product/-1", PUT, entity, ProductOutput.class);

        assertEquals(HttpStatus.BAD_REQUEST, productOutputResponseEntity.getStatusCode());
    }

    @Test
    public void deleteShop() throws Exception {
        ProductInput productInput = new ProductInput();
        productInput.setName("hjkhjk");
        productInput.setDescription("cards for playing");
        productInput.setPrice((float)6);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductInput> entity = new HttpEntity<ProductInput>(productInput, headers);
        ResponseEntity<ProductOutput> productOutputResponseEntity = testRestTemplate.exchange("/shop/1/product/create", POST, entity, ProductOutput.class);

        ResponseEntity<ProductOutput> productOutputResponseEntity2 = testRestTemplate.exchange("/product/" + productOutputResponseEntity.getBody().getProductId(), DELETE, entity, ProductOutput.class);

        assertEquals(HttpStatus.OK, productOutputResponseEntity2.getStatusCode());
    }

    @Test
    public void deleteFakeShop() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ProductOutput> productOutputResponseEntity2 = testRestTemplate.exchange("/product/-1", DELETE, null, ProductOutput.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, productOutputResponseEntity2.getStatusCode());
    }

}*/
