package com.shopify.demo.repositories;

import com.shopify.demo.models.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineItemJPARepository extends JpaRepository<LineItem, Integer> {

    LineItem findLineItemByLineItemId(Integer lineItemId);

    List<LineItem> findAllByOrderId(Integer orderId);

    List<LineItem> findAllByProductId(Integer productId);
}
