package com.shopify.demo.controllers;

import com.shopify.demo.models.iomodels.*;
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
import static org.springframework.http.HttpMethod.PUT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShopControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ShopRepository shopRepository;

    @Test
    public void getCreatedShopOutput() throws Exception {
        ShopInput shopInput = new ShopInput();
        shopInput.setName("Shopify");
        shopInput.setDescription("Get your business up and running with our e-commerce platform!");
//        shopInput.setUserId(123);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ShopInput> entity = new HttpEntity<ShopInput>(shopInput, headers);
        ResponseEntity<ShopOutput> shopOutputResponseEntity = testRestTemplate.postForEntity("/shop/create", entity, ShopOutput.class);

        ShopOutput shopOutput = shopOutputResponseEntity.getBody();

        assertEquals("Shopify", shopOutputResponseEntity.getBody().getName());
        assertEquals("Get your business up and running with our e-commerce platform!", shopOutputResponseEntity.getBody().getDescription());
        assertEquals(123, shopOutputResponseEntity.getBody().getUserId().intValue());
        assertNotNull(shopOutputResponseEntity.getBody().getShopId());

        shopRepository.deleteShopById(shopOutputResponseEntity.getBody().getShopId());
    }

    @Test
    public void getShopById() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ShopHeavyOutput> shopHeavyOutputResponseEntity = testRestTemplate.getForEntity("/shop/1", ShopHeavyOutput.class);

        assertEquals("House of Cards" , shopHeavyOutputResponseEntity.getBody().getName());
    }
/*
    @Test
    public void getProductsOfShopById() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ProductListHeavyWrapper> productListHeavyWrapperResponseEntity = testRestTemplate.getForEntity("/shop/1/product/all", ProductListHeavyWrapper.class);

        assertEquals(1 , productListHeavyWrapperResponseEntity.getBody().getProducts().size());
    }*/

    @Test
    public void getProductsOfFakeShopById() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ProductListHeavyWrapper> productListHeavyWrapperResponseEntity = testRestTemplate.getForEntity("/shop/-1/product/all", ProductListHeavyWrapper.class);

        assertEquals(0 , productListHeavyWrapperResponseEntity.getBody().getProducts().size());
    }

    @Test
    public void getFakeShop() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ShopHeavyOutput> shopHeavyOutputResponseEntity = testRestTemplate.getForEntity("/shop/4", ShopHeavyOutput.class);

        assertEquals(HttpStatus.BAD_REQUEST, shopHeavyOutputResponseEntity.getStatusCode());
    }

    @Test
    public void createShopWithNoName() throws Exception {
        ShopInput shopInput = new ShopInput();
        shopInput.setName("");
        shopInput.setDescription("Get your business up and running with our e-commerce platform!");
//        shopInput.setUserId(123);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ShopInput> entity = new HttpEntity<ShopInput>(shopInput, headers);
        ResponseEntity<ShopOutput> shopOutputResponseEntity = testRestTemplate.postForEntity("/shop/create", entity, ShopOutput.class);

        assertEquals(HttpStatus.BAD_REQUEST, shopOutputResponseEntity.getStatusCode());
    }

    @Test
    public void createShopWithDuplicateName() throws Exception {
        ShopInput shopInput = new ShopInput();
        shopInput.setName("Shopify");
        shopInput.setDescription("Get your business up and running with our e-commerce platform!");
//        shopInput.setUserId(123);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ShopInput> entity = new HttpEntity<ShopInput>(shopInput, headers);
        ResponseEntity<ShopOutput> shopOutputResponseEntity = testRestTemplate.postForEntity("/shop/create", entity, ShopOutput.class);


        ShopInput shopInput2 = new ShopInput();
        shopInput2.setName("Shopify");
        shopInput2.setDescription("Get your business up and running with our e-commerce platform!");
//        shopInput2.setUserId(123);

        HttpEntity<ShopInput> entity2 = new HttpEntity<ShopInput>(shopInput2, headers);
        ResponseEntity<ShopOutput> shopOutputResponseEntity2 = testRestTemplate.postForEntity("/shop/create", entity2, ShopOutput.class);

        assertEquals(HttpStatus.BAD_REQUEST, shopOutputResponseEntity2.getStatusCode());

        shopRepository.deleteShopById(shopOutputResponseEntity.getBody().getShopId());
    }

    @Test
    public void updateShopWithNoName() throws Exception {
        ShopInput shopInput = new ShopInput();
        shopInput.setName("");
        shopInput.setDescription("Get your business up and running with our e-commerce platform!");
//        shopInput.setUserId(123);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ShopInput> entity = new HttpEntity<ShopInput>(shopInput, headers);
        ResponseEntity<ShopOutput> shopOutputResponseEntity = testRestTemplate.exchange("/shop/1", PUT, entity, ShopOutput.class);

        assertEquals(HttpStatus.BAD_REQUEST, shopOutputResponseEntity.getStatusCode());
    }

    @Test
    public void updateFakeShop() throws Exception {
        ShopInput shopInput = new ShopInput();
        shopInput.setName("ban");
        shopInput.setDescription("Get your business up and running with our e-commerce platform!");
//        shopInput.setUserId(123);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ShopInput> entity = new HttpEntity<ShopInput>(shopInput, headers);
        ResponseEntity<ShopOutput> shopOutputResponseEntity = testRestTemplate.exchange("/shop/-1", PUT, entity, ShopOutput.class);

        assertEquals(HttpStatus.BAD_REQUEST, shopOutputResponseEntity.getStatusCode());
    }

    @Test
    public void deleteShop() throws Exception {
        ShopInput shopInput = new ShopInput();
        shopInput.setName("Shopify");
        shopInput.setDescription("Get your business up and running with our e-commerce platform!");
//        shopInput.setUserId(123);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ShopInput> entity = new HttpEntity<ShopInput>(shopInput, headers);
        ResponseEntity<ShopOutput> shopOutputResponseEntity = testRestTemplate.postForEntity("/shop/create", entity, ShopOutput.class);

        ResponseEntity<ShopOutput> shopOutputResponseEntity2 = testRestTemplate.exchange(
                "/shop/" + shopOutputResponseEntity.getBody().getShopId(),
                DELETE,
                entity,
                ShopOutput.class);

        assertEquals(HttpStatus.OK, shopOutputResponseEntity2.getStatusCode());
    }

    @Test
    public void deleteFakeShop() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ShopOutput> shopOutputResponseEntity2 = testRestTemplate.exchange(
                "/shop/-1",
                DELETE,
                null,
                ShopOutput.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, shopOutputResponseEntity2.getStatusCode());
    }
}