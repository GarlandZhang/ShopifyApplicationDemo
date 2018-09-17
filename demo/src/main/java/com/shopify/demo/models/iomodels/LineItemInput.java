package com.shopify.demo.models.iomodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LineItemInput {
    Integer productId;
    String properties;
    Integer quantity;
    Float discount;
}
