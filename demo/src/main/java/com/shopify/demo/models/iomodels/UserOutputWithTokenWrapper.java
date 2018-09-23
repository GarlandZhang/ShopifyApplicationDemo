package com.shopify.demo.models.iomodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.test.context.SpringBootTest;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserOutputWithTokenWrapper {
    UserOutput userOutput;
    String token;
}
