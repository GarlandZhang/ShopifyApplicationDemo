package com.shopify.demo.models.iomodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Message {
    String message;

    public Message() {
        message = "";
    }

    public Message(String message) {
        this();
        this.message = message;
    }
}
