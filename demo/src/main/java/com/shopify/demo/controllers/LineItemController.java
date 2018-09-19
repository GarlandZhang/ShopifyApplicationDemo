package com.shopify.demo.controllers;

import com.shopify.demo.models.LineItem;
import com.shopify.demo.models.Order;
import com.shopify.demo.models.Product;
import com.shopify.demo.models.iomodels.*;
import com.shopify.demo.repositories.LineItemRepository;
import com.shopify.demo.repositories.OrderRepository;
import com.shopify.demo.repositories.ProductRepository;
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
public class LineItemController {

    @Autowired
    LineItemRepository lineItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderController orderController;

    /**
     * createOrderAndAddLineItem: adds a new Line Item to a newly created order
     */
    @PostMapping("/shop/{shopId}/order/new/line-item/create/for/product/{productId}")
    private ResponseEntity<LineItemOutput> createOrderAndAddLineItem(@RequestBody LineItemInput lineItem, @PathVariable Integer shopId, @PathVariable Integer productId) throws Exception {
        // check if properties defined in lineItem are valid
        if(invalidLineItem(lineItem)) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Invalid arguments passed")
                .body(null);

        Product product = productRepository.getProductById(productId);
        Order order = orderController.internalCreateOrder(new OrderInput(), shopId);

        if(order == null || product == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Create unsuccessful")
                .body(null);


        // return a newly created line item
        LineItem newLineItem = updateLineItemByIdWithFlag(lineItem, -1, order.getOrderId(), productId,true);

        if(newLineItem == null) {
            orderRepository.deleteOrderById(order.getOrderId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Status", "400: Create unsuccessful")
                    .body(null);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Create successful")
                .body(new LineItemOutput(newLineItem));
    }

    /**
     * addLineItemToOrder: adds a new Line Item, with properties in lineItem, to the order with orderId
     * @param lineItem
     * @param orderId
     * @return the newly created LineItem
     */
    @PostMapping("/order/{orderId}/line-item/create/for/product/{productId}")
    private ResponseEntity<LineItemOutput> addLineItemToOrder(@RequestBody LineItemInput lineItem,
                                                              @PathVariable Integer orderId,
                                                              @PathVariable Integer productId) throws Exception {

        // check if properties defined in lineItem are valid
        if(invalidLineItem(lineItem)) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Invalid arguments passed")
                .body(null);

        // return a newly created line item
        LineItem newLineItem = updateLineItemByIdWithFlag(lineItem, -1, orderId, productId, true);
        
        if(newLineItem == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Create unsuccessful")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Create successful")
                .body(new LineItemOutput(newLineItem));
    }

    /**
     * invalidLineItem: checks if a lineItem has invalid properties such as quantity <= 0
     * @param lineItem
     * @return true if invalid in any way
     */
    private boolean invalidLineItem(LineItemInput lineItem) {
        return lineItem == null
                || (lineItem.getDiscount() != null && (lineItem.getDiscount() < 0 || lineItem.getDiscount() > 1))
                || lineItem.getQuantity() == null
                || lineItem.getQuantity() <= 0;
    }

    /**
     * updateLineItemByIdWithFlag: updates an existing Line Item with id, lineItemId, with the new properties in updateLineItem
     *  and correspondingly updates the parent order with id, orderId; createNewFlag checks if this is a new Line Item to generate
     * @param updateLineItem
     * @param lineItemId
     * @param orderId
     * @param createNewFlag
     * @effects updates parent order with new total
     * @return the newly generated or updated Line Item
     * @throws Exception
     */
    private LineItem updateLineItemByIdWithFlag(@RequestBody LineItemInput updateLineItem,
                                                      Integer lineItemId,
                                                      Integer orderId,
                                                      Integer productId,
                                                      boolean createNewFlag) throws Exception {

        LineItem lineItem = null;
        Order order = null;

        // check if this is a newly created Line Item
        if(createNewFlag) {
            lineItem = new LineItem();

            // fetch product; set price and product id of parent product
            Product product = productRepository.getProductById(productId);
            if(product == null) return null;
            lineItem.setProductId(productId);
            lineItem.setPrice(product.getPrice());

            // fetch order; set order id of parent order
            order = orderRepository.getOrderById(orderId);
            if(order == null) return null;
            lineItem.setOrderId(orderId);
        } else{
            // fetch line item
            lineItem = lineItemRepository.getLineItemById(lineItemId);
            if(lineItem == null) return null;

            // fetch order
            order = orderRepository.getOrderById(lineItem.getOrderId());
            if(order == null) return null;

            // remove previous amount
            order.setTotal(order.getTotal() - calcTotalLineItem(lineItem));
        }

        // update with new values
        lineItem.setProperties(updateLineItem.getProperties() == null? "" : updateLineItem.getProperties());
        lineItem.setQuantity(updateLineItem.getQuantity() == null? 0 : updateLineItem.getQuantity());
        lineItem.setDiscount(updateLineItem.getDiscount() == null? 0 : updateLineItem.getDiscount());
        order.setTotal(order.getTotal() + calcTotalLineItem(lineItem));

        lineItem = lineItemRepository.saveLineItem(lineItem);
        orderRepository.saveOrder(order);

        return lineItem;
    }

    /**
     * calcTotalLineItem: calculates the total of the lineItem
     * @param lineItem
     * @return the total of the lineItem
     */
    Float calcTotalLineItem(LineItem lineItem) {
        return lineItem.getPrice() * lineItem.getQuantity() * (1-lineItem.getDiscount());
    }

    /**
     * getLineItems: gets all Line Items
     * *note: Use case would be for ADMIN only.
     * @return list of all List Items
     */
    @GetMapping("/line-item/all")
    private ResponseEntity<LineItemListWrapper> getLineItems() {
        List<LineItem> lineItems = lineItemRepository.getAll();

        if(lineItems == null) return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new LineItemListWrapper());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new LineItemListWrapper(lineItems));
    }

    @GetMapping("/line-item/{lineItemId}")
    private ResponseEntity<LineItemOutput> getLineItemById(@PathVariable Integer lineItemId) throws Exception {
        LineItem lineItem = lineItemRepository.getLineItemById(lineItemId);

        if(lineItem == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Line Item does not exist with id: " + lineItemId)
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new LineItemOutput(lineItem));
    }

    /**
     * getProductWithLineItemId: gets the parent Product to the Line Item with id, lineItemId
     * @param lineItemId
     * @return the parent Product
     * @throws Exception
     */
    @GetMapping("/line-item/{lineItemId}/product")
    private ResponseEntity<ProductHeavyOutput> getProductWithLineItemId(@PathVariable Integer lineItemId) throws Exception {
        Product product = productRepository.getProductByLineItemId(lineItemId);

        if(product == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Line Item does not exist with id: " + lineItemId)
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ProductHeavyOutput(product));
    }

    /**
     * getOrderWithLineItemId: gets parent Order of the Line Item with id, lineItemId
     * @param lineItemId
     * @return the parent Order
     * @throws Exception
     */
    @GetMapping("/line-item/{lineItemId}/order")
    private ResponseEntity<OrderHeavyOutput> getOrderWithLineItemId(@PathVariable Integer lineItemId) throws Exception {
        Order order = orderRepository.getOrderByLineItemId(lineItemId);

        if(order == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Line Item does not exist with id: " + lineItemId)
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new OrderHeavyOutput(order));
    }

    /**
     * updateLineItemId: updates Line Item with id, lineItemId, using properties in lineItem
     * @param lineItem
     * @param lineItemId
     * @return the updated LineItem
     * @throws Exception
     */
    @PutMapping("/line-item/{lineItemId}")
    private ResponseEntity<LineItemOutput> updateLineItemId(@RequestBody LineItemInput lineItem, @PathVariable Integer lineItemId) throws Exception {

        LineItem existingLineItem = lineItemRepository.getLineItemById(lineItemId);
        if(existingLineItem == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Line Item does not exist with id: " + lineItemId)
                .body(null);

        if(invalidLineItem(lineItem)) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Invalid arguments passed")
                .body(null);

        LineItem updatedLineItem = updateLineItemByIdWithFlag(lineItem, lineItemId, -1, -1, false);
        
        if(lineItem == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Status", "500: Update unsuccessful")
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new LineItemOutput(updatedLineItem));
    }

    /**
     * deleteLineItemId: deletes Line Item with id, lineItemId
     * @param lineItemId
     * @return message of successful deletion
     * @throws Exception
     */
    @DeleteMapping("/line-item/{lineItemId}")
    private ResponseEntity<Message> deleteLineItemId(@PathVariable Integer lineItemId) throws Exception {

        LineItem lineItem = lineItemRepository.getLineItemById(lineItemId);
        if(lineItem == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: Line Item does not exist with id: " + lineItemId)
                .body(new Message("Line Item does not exist with id:" + lineItemId));

        Order order = orderRepository.getOrderByLineItemId(lineItemId);
        if(order == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Status", "500: Could not successfully retrieve Order")
                .body(new Message("Could not successfully retrieve Order with id :" + lineItemId));


        try{
            if(order.getLineItems().size() == 1){
                orderRepository.deleteOrderById(order.getOrderId());
            } else {
                // set new total
                order.setTotal(order.getTotal() - calcTotalLineItem(lineItem));
                orderRepository.saveOrder(order);
                lineItemRepository.deleteLineItemById(lineItemId);
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Status", "200: Delete successful")
                    .body(new Message("Delete successful"));
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Status", "500: Delete unsuccessful")
                    .body(new Message("Delete unsuccessful"));
        }
    }


}
