package com.shopify.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="`User`")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer userId;
    String userName;
    String token;
    String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Shop> shops;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Order> orders;

}
