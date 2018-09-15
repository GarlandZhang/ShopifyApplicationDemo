package com.shopify.demo.repositories;

import com.shopify.demo.models.LineItem;

import java.util.List;

public interface LineItemRepository {

    LineItem getLineItemById(Integer lineItemId);

    LineItem getLineItemByIdAndMinify(Integer lineItemId);

    void saveLineItem(LineItem lineItem);

    List<LineItem> getAllByOrderId(Integer orderId);

    List<LineItem> getAllByOrderIdAndMinify(Integer orderId);

    void deleteLineItemById(Integer lineItemId);

    List<LineItem> getAllByProductId(Integer productId);

    List<LineItem> getAllByProductIdAndMinify(Integer productId);
}
