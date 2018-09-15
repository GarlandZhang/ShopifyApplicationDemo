package com.shopify.demo.controllers;

import com.shopify.demo.models.LineItem;
import com.shopify.demo.models.Order;
import com.shopify.demo.models.Product;
import com.shopify.demo.repositories.LineItemRepository;
import com.shopify.demo.repositories.OrderRepository;
import com.shopify.demo.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    /**
     * addLineItemToOrder: adds a new Line Item, with properties in lineItem, to the order with orderId
     * @param lineItem
     * @param orderId
     * @return the newly created LineItem
     */
    @PostMapping("/order/{orderId}/line-item/create")
    private LineItem addLineItemToOrder(@RequestBody LineItem lineItem, @PathVariable Integer orderId) throws Exception {

        // check if properties defined in lineItem are valid
        if(invalidLineItem(lineItem)) throw new Exception("Invalid input");

        // return a newly created line item
        return updateLineItemByIdWithFlag(lineItem, -1, orderId, true);
    }

    /**
     * invalidLineItem: checks if a lineItem has invalid properties such as quantity <= 0
     * @param lineItem
     * @return true if invalid in any way
     */
    private boolean invalidLineItem(LineItem lineItem) {
        return lineItem == null
                || lineItem.getProductId() == null
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
    private LineItem updateLineItemByIdWithFlag(@RequestBody LineItem updateLineItem,
                                                Integer lineItemId ,
                                                Integer orderId,
                                                boolean createNewFlag) throws Exception {

        LineItem lineItem = null;
        Order order = null;

        // check if this is a newly created Line Item
        if(createNewFlag) {
            lineItem = new LineItem();

            // fetch product; set price and product id of parent product
            Product product = productRepository.getProductByIdMin(updateLineItem.getProductId());
            if(product == null) throw new Exception("500: Product with this id does not exist");
            lineItem.setProductId(updateLineItem.getProductId());
            lineItem.setPrice(product.getPrice());

            // fetch order; set order id of parent order
            order = orderRepository.getOrderById(orderId);
            if(order == null) throw new Exception("500: Order with this id does not exist");
            lineItem.setOrderId(orderId);
        } else{
            // fetch line item
            lineItem = lineItemRepository.getLineItemByIdMin(lineItemId);
            if(lineItem == null) throw new Exception("500: Line Item does not exist");

            // fetch order
            order = orderRepository.getOrderById(lineItem.getOrderId());
            if(order == null) throw new Exception("500: Order with this id does not exist");

            // remove previous amount
            order.setTotal(order.getTotal() - calcTotalLineItem(lineItem));
        }

        // update with new values
        lineItem.setProperties(updateLineItem.getProperties() == null? "" : updateLineItem.getProperties());
        lineItem.setQuantity(updateLineItem.getQuantity() == null? 0 : updateLineItem.getQuantity());
        lineItem.setDiscount(updateLineItem.getDiscount() == null? 0 : updateLineItem.getDiscount());
        order.setTotal(order.getTotal() + calcTotalLineItem(lineItem));

        lineItemRepository.saveLineItem(lineItem);
        orderRepository.save(order);

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

    @GetMapping("/line-item/{lineItemId}")
    private LineItem getLineItemById(@PathVariable Integer lineItemId) throws Exception {
        LineItem lineItem = lineItemRepository.getLineItemByIdMin(lineItemId);

        if(lineItem == null) throw new Exception("500: Line Item with this id does not exist");

        return lineItem;
    }

    /**
     * getProductWithLineItemId: gets the parent Product to the Line Item with id, lineItemId
     * @param lineItemId
     * @return the parent Product
     * @throws Exception
     */
    @GetMapping("/line-item/{lineItemId}/product")
    private Product getProductWithLineItemId(@PathVariable Integer lineItemId) throws Exception {
        Product product = productRepository.getProductByLineItemIdMin(lineItemId);

        if(product == null) throw new Exception("500: Line Item does not belong to a product");

        return product;
    }

    /**
     * getOrderWithLineItemId: gets parent Order of the Line Item with id, lineItemId
     * @param lineItemId
     * @return the parent Order
     * @throws Exception
     */
    @GetMapping("/line-item/{lineItemId}/order")
    private Order getOrderWithLineItemId(@PathVariable Integer lineItemId) throws Exception {
        Order order = orderRepository.getOrderByLineItemIdMin(lineItemId);

        if(order == null) throw new Exception("500: Line Item does not belong to an order");

        return order;
    }

    /**
     * updateLineItemId: updates Line Item with id, lineItemId, using properties in lineItem
     * @param lineItem
     * @param lineItemId
     * @return the updated LineItem
     * @throws Exception
     */
    @PutMapping("/line-item/{lineItemId}")
    private LineItem updateLineItemId(@RequestBody LineItem lineItem, @PathVariable Integer lineItemId) throws Exception {

        LineItem existingLineItem = lineItemRepository.getLineItemByIdMin(lineItemId);
        if(existingLineItem == null) throw new Exception("500");

        if(invalidLineItem(lineItem)) throw new Exception("500: Invalid input");

        return updateLineItemByIdWithFlag(lineItem, lineItemId, -1, false);
    }

    /**
     * deleteLineItemId: deletes Line Item with id, lineItemId
     * @param lineItemId
     * @return message of successful deletion
     * @throws Exception
     */
    @DeleteMapping("/line-item/{lineItemId}")
    private String deleteLineItemId(@PathVariable Integer lineItemId) throws Exception {

        //
        LineItem lineItem = lineItemRepository.getLineItemByIdMin(lineItemId);
        if(lineItem == null) throw new Exception("Line Item does not exist with this id");

        Order order = orderRepository.getOrderByLineItemIdMin(lineItemId);
        if(order == null) throw new Exception("Order does not exist for this line item");

        order.setTotal(order.getTotal() - calcTotalLineItem(lineItem));
        orderRepository.save(order);

        lineItemRepository.deleteLineItemById(lineItemId);

        return "200: Successfully deleted";
    }

}
