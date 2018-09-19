package com.shopify.demo.controllers;

import com.shopify.demo.models.Order;
import com.shopify.demo.models.iomodels.*;
import com.shopify.demo.repositories.LineItemRepository;
import com.shopify.demo.repositories.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class LineItemControllerTest {


    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LineItemRepository lineItemRepository;

    @Autowired
    OrderController orderController;


    @Test
    public void getCreatedLineItemOutput() throws Exception {
        LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setProperties("Cookies delicious");
        lineItemInput.setDiscount((float) .5);
        lineItemInput.setQuantity(10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.postForEntity("/shop/1/order/new/line-item/create/for/product/1",
                lineItemInput, LineItemOutput.class);

        assertEquals("Cookies delicious", responseEntity.getBody().getProperties());

        orderRepository.deleteOrderById(responseEntity.getBody().getOrderId());

        assertNull(lineItemRepository.getLineItemById(responseEntity.getBody().getLineItemId()));
//        lineItemRepository.deleteLineItemById(responseEntity.getBody().getLineItemId());
    }

    @Test
    public void getCreatedLineItemOutputFakeShopId() throws Exception {
        LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setProperties("Cookies delicious");
        lineItemInput.setDiscount((float) .5);
        lineItemInput.setQuantity(10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.postForEntity("/shop/-1/order/new/line-item/create/for/product/1",
                lineItemInput, LineItemOutput.class);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getCreatedLineItemOutputFakeProductId() throws Exception {
        LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setProperties("Cookies delicious");
        lineItemInput.setDiscount((float) .5);
        lineItemInput.setQuantity(10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.postForEntity("/shop/1/order/new/line-item/create/for/product/-1",
                lineItemInput, LineItemOutput.class);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getCreatedLineItemOutputFakeProductAndShopId() throws Exception {
        LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setProperties("Cookies delicious");
        lineItemInput.setDiscount((float) .5);
        lineItemInput.setQuantity(10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.postForEntity("/shop/-1/order/new/line-item/create/for/product/-1",
                lineItemInput, LineItemOutput.class);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getCreatedLineItemOutputBadQuantity() throws Exception {
        LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setProperties("Cookies delicious");
        lineItemInput.setDiscount((float) .5);
        lineItemInput.setQuantity(0);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.postForEntity("/shop/-1/order/new/line-item/create/for/product/-1",
                lineItemInput, LineItemOutput.class);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());

        lineItemInput.setQuantity(-1);
        entity = new HttpEntity<>(lineItemInput, headers);
        responseEntity = testRestTemplate.postForEntity("/shop/-1/order/new/line-item/create/for/product/-1",
                lineItemInput, LineItemOutput.class);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getCreatedLineItemOutputBadDiscount() throws Exception {
        LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setProperties("Cookies delicious");
        lineItemInput.setDiscount((float) 2);
        lineItemInput.setQuantity(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.postForEntity("/shop/-1/order/new/line-item/create/for/product/-1",
                lineItemInput, LineItemOutput.class);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());

        lineItemInput.setDiscount((float) -1);
        entity = new HttpEntity<>(lineItemInput, headers);
        responseEntity = testRestTemplate.postForEntity("/shop/-1/order/new/line-item/create/for/product/-1",
                lineItemInput, LineItemOutput.class);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }

/*
    @Test
    public void getCreatedLineItemForOrderOutput() throws Exception {
        LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setProperties("Cookies delicious");
        lineItemInput.setDiscount((float) .5);
        lineItemInput.setQuantity(10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.postForEntity("/order/1/line-item/create/for/product/1",
                lineItemInput, LineItemOutput.class);

        assertEquals("Cookies delicious", responseEntity.getBody().getProperties());

        lineItemRepository.deleteLineItemById(2);

        Order order = orderRepository.getOrderById(1);

        assertEquals(1, lineItemRepository.getAll().size());
        assertEquals(1, orderRepository.getOrderById(1).getLineItems().size());
        order.setTotal(order.getTotal() - responseEntity.getBody().getDiscount() * responseEntity.getBody().getPrice() * responseEntity.getBody().getQuantity());
        orderRepository.saveOrder(order);
    }
*/

    @Test
    public void getCreatedLineItemForFakeOrderOutput() throws Exception {
        LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setProperties("Cookies delicious");
        lineItemInput.setDiscount((float) .5);
        lineItemInput.setQuantity(10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.postForEntity("/order/-1/line-item/create/for/product/1",
                lineItemInput, LineItemOutput.class);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getCreatedLineItemForFakeProductOutput() throws Exception {
        LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setProperties("Cookies delicious");
        lineItemInput.setDiscount((float) .5);
        lineItemInput.setQuantity(10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.postForEntity("/order/1/line-item/create/for/product/-1",
                lineItemInput, LineItemOutput.class);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getCreatedLineItemForFakeOrderAndProductOutput() throws Exception {
        LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setProperties("Cookies delicious");
        lineItemInput.setDiscount((float) .5);
        lineItemInput.setQuantity(10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.postForEntity("/order/-1/line-item/create/for/product/-1",
                lineItemInput, LineItemOutput.class);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getLineItemById() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.getForEntity("/line-item/1", LineItemOutput.class);

        assertEquals("Extra info here: ...", responseEntity.getBody().getProperties());
    }

    @Test
    public void getFakeLineItemById() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.getForEntity("/line-item/-1", LineItemOutput.class);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void updateLineItem() throws Exception {
        LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setQuantity(20);
        lineItemInput.setDiscount((float) .3);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.exchange("/line-item/1", PUT, entity, LineItemOutput.class);

        assertEquals(20, responseEntity.getBody().getQuantity().intValue());

        Order order = orderRepository.getOrderByLineItemId(responseEntity.getBody().getLineItemId());

        assertEquals(154.00, order.getTotal().floatValue(), .1);
    }

    @Test
    public void updateLineItemWith2() throws Exception {

        LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setProperties("Cookies delicious");
        lineItemInput.setDiscount((float) .5);
        lineItemInput.setQuantity(10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.postForEntity("/order/1/line-item/create/for/product/1",
                lineItemInput, LineItemOutput.class);


        LineItemInput lineItemInput2 = new LineItemInput();
        lineItemInput2.setQuantity(20);
        lineItemInput2.setDiscount((float) .3);

        HttpEntity<LineItemInput> entity2 = new HttpEntity<>(lineItemInput2, headers);
        ResponseEntity<LineItemOutput> responseEntity2 = testRestTemplate.exchange("/line-item/" + responseEntity.getBody().getLineItemId(), PUT, entity2, LineItemOutput.class);

        assertEquals(20, responseEntity2.getBody().getQuantity().intValue());

        Order order = orderRepository.getOrderByLineItemId(responseEntity2.getBody().getLineItemId());

        assertEquals(171.60, order.getTotal().floatValue(), .1);


        lineItemRepository.deleteLineItemById(responseEntity.getBody().getLineItemId());

        order.setTotal(order.getTotal() - (1-responseEntity2.getBody().getDiscount()) * responseEntity2.getBody().getPrice() * responseEntity2.getBody().getQuantity());
        orderRepository.saveOrder(order);

        assertEquals(17.60, order.getTotal().floatValue(), 1);
    }

    @Test
    public void updateLineItemFakeId() throws Exception {

        LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setProperties("Cookies delicious");
        lineItemInput.setDiscount((float) .5);
        lineItemInput.setQuantity(10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.exchange("/line-item/-1", PUT,
                entity, LineItemOutput.class);


        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    public void deleteFakeLineItem() throws Exception {
            LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setQuantity(20);
        lineItemInput.setDiscount((float) .3);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<Message> responseEntity = testRestTemplate.exchange("/line-item/-1", DELETE, entity, Message.class);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void deleteLineItem() throws Exception {

        LineItemInput lineItemInput = new LineItemInput();
        lineItemInput.setProperties("Cookies delicious");
        lineItemInput.setDiscount((float) .5);
        lineItemInput.setQuantity(10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LineItemInput> entity = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<LineItemOutput> responseEntity = testRestTemplate.postForEntity("/shop/1/order/new/line-item/create/for/product/1",
                lineItemInput, LineItemOutput.class);

        Order temp = orderRepository.getOrderById(responseEntity.getBody().getOrderId());
        assertNotNull(temp);
        assertEquals(55, temp.getTotal().floatValue(), .1);

        HttpEntity<LineItemInput> entity2 = new HttpEntity<>(lineItemInput, headers);
        ResponseEntity<Message> responseEntity2 = testRestTemplate.exchange("/line-item/2", DELETE, entity2, Message.class);

        assertNull(orderRepository.getOrderById(temp.getOrderId()));
        assertNull(lineItemRepository.getLineItemById(responseEntity.getBody().getLineItemId()));
    }

}