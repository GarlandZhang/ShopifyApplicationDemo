/*
package com.shopify.demo.controllers;

import com.shopify.demo.models.Order;
import com.shopify.demo.models.iomodels.*;
import com.shopify.demo.repositories.OrderRepository;
import com.shopify.demo.repositories.ProductRepository;
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
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    OrderController orderController;

    @Test
    public void getInternallyCreatedOrder() throws Exception {
        OrderInput orderInput = new OrderInput();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Order order = orderController.internalCreateOrder(orderInput, 1);

        assertEquals(1, order.getShopId().intValue());

        orderRepository.deleteOrderById(order.getOrderId());
    }

    @Test
    public void getInternallyCreatedOrderWithInvalidId() throws Exception {
        OrderInput orderInput = new OrderInput();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Order order = orderController.internalCreateOrder(orderInput, -1);

        assertNull(order);
    }
*/
/*
    @Test
    public void getOrderById() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<OrderHeavyOutput> responseEntity = testRestTemplate.getForEntity("/order/1", OrderHeavyOutput.class);

        assertEquals(17.6 , responseEntity.getBody().getTotal().floatValue(), .1);
    }*//*


    @Test
    public void getOrderByFakeId() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<OrderHeavyOutput> responseEntity = testRestTemplate.getForEntity("/order/-1", OrderHeavyOutput.class);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }

*/
/*    @Test
    public void getLineItems() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<LineItemListWrapper> responseEntity = testRestTemplate.getForEntity("/order/1/line-item/all", LineItemListWrapper.class);

        assertEquals(1, responseEntity.getBody().getLineItems().size());
    }*//*


    @Test
    public void getLineItemsWithFakeId() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<LineItemListWrapper> responseEntity = testRestTemplate.getForEntity("/order/-1/line-item/all", LineItemListWrapper.class);

        assertEquals(0, responseEntity.getBody().getLineItems().size());
    }

    @Test
    public void updateFakeOrder() throws Exception {
        OrderInput orderInput = new OrderInput();
        orderInput.setStatus("COMPLETE");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderInput> entity = new HttpEntity<>(orderInput, headers);
        ResponseEntity<OrderHeavyOutput> responseEntity = testRestTemplate.exchange("/order/-1", PUT, entity, OrderHeavyOutput.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void updateOrderWithWrongStatus() throws Exception {
        OrderInput orderInput = new OrderInput();
        orderInput.setStatus("COMPLETEZZZ");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderInput> entity = new HttpEntity<>(orderInput, headers);
        ResponseEntity<OrderHeavyOutput> responseEntity = testRestTemplate.exchange("/order/1", PUT, entity, OrderHeavyOutput.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void updateOrder() throws Exception {
        OrderInput orderInput = new OrderInput();
        orderInput.setStatus("COMPLETE");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderInput> entity = new HttpEntity<>(orderInput, headers);
        ResponseEntity<OrderHeavyOutput> responseEntity = testRestTemplate.exchange("/order/1", PUT, entity, OrderHeavyOutput.class);

        assertEquals("COMPLETE", responseEntity.getBody().getStatus());
    }

    @Test
    public void deleteOrder() throws Exception {
        OrderInput orderInput = new OrderInput();
        orderInput.setStatus("COMPLETE");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderInput> entity = new HttpEntity<>(orderInput, headers);
        ResponseEntity<OrderHeavyOutput> responseEntityTemp =
                testRestTemplate.exchange("/shop/1/order/create", POST, entity, OrderHeavyOutput.class);

        Order order = orderController.internalCreateOrder(orderInput, 1);

        ResponseEntity<Message> responseEntity =
                testRestTemplate.exchange("/order/" + order.getOrderId(), DELETE, entity, Message.class);

        assertEquals(OK, responseEntity.getStatusCode());
    }

    @Test
    public void deleteFakeOrder() throws Exception {
        OrderInput orderInput = new OrderInput();
        orderInput.setStatus("COMPLETE");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderInput> entity = new HttpEntity<>(orderInput, headers);
        ResponseEntity<Message> responseEntity = testRestTemplate.exchange("/order/-1", DELETE, entity, Message.class);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }
}*/
