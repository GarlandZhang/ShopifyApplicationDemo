package com.shopify.demo.models.iomodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class OrderInput {
    String status;

    public OrderInput() {
        status = "INCOMPLETE";
    }

    public OrderInput(String status) {
        this.status = status;
    }
}
