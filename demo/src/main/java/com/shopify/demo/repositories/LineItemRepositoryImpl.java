package com.shopify.demo.repositories;

import com.shopify.demo.models.LineItem;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@NoArgsConstructor
public class LineItemRepositoryImpl implements LineItemRepository{

    @Autowired
    LineItemJPARepository lineItemJPARepository;

    @Override
    public LineItem getLineItemById(Integer lineItemId) {
        return lineItemJPARepository.findLineItemByLineItemId(lineItemId);
    }

    @Override
    public LineItem getLineItemByIdMin(Integer lineItemId) {
        return minify(getLineItemById(lineItemId));
    }

    LineItem minify(LineItem lineItem) {
        if(lineItem != null) {
            lineItem.setProduct(null);
            lineItem.setOrder(null);
        }

        return lineItem;
    }

    @Override
    public void saveLineItem(LineItem lineItem) {
        lineItemJPARepository.save(lineItem);
    }


    @Override
    public List<LineItem> getAllByOrderId(Integer orderId) {
        return lineItemJPARepository.findAllByOrderId(orderId);
    }

    @Override
    public List<LineItem> getAllByOrderIdMin(Integer orderId) {
        List<LineItem> lineItems = getAllByOrderId(orderId);

        if(lineItems != null) {
            for(LineItem lineItem : lineItems) {
                minify(lineItem);
            }
        }

        return lineItems;
    }

    @Override
    public void deleteLineItemById(Integer lineItemId) {
        lineItemJPARepository.deleteById(lineItemId);
    }

    @Override
    public List<LineItem> getAllByProductId(Integer productId) {
        return lineItemJPARepository.findAllByProductId(productId);
    }

    @Override
    public List<LineItem> getAllByProductIdMin(Integer productId) {
        List<LineItem> lineItems = getAllByProductId(productId);

        if(lineItems != null) {
            for(LineItem lineItem : lineItems) {
                minify(lineItem);
            }
        }

        return lineItems;
    }
}
