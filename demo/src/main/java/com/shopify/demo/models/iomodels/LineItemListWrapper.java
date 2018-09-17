package com.shopify.demo.models.iomodels;

import com.shopify.demo.models.LineItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LineItemListWrapper {
    List<LineItemOutput> lineItems;

    public LineItemListWrapper() {
        lineItems = new ArrayList<>();
    }

    public LineItemListWrapper(List<LineItem> lineItems) {
        this();
        if(lineItems != null) {
            for(LineItem lineItem : lineItems) {
                this.lineItems.add(new LineItemOutput(lineItem));
            }
        }
    }
}
