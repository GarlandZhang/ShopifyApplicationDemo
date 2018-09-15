package com.shopify.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.sound.sampled.Line;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="`Order`" , schema="dbo")
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer orderId;
    Integer shopId;
//    Integer customerId;
    Date creationDate;
    Date updateDate;
    Float total;

    @ManyToOne
    @JoinColumn(name="shopId", insertable=false, updatable=false)
    Shop shop;

/*
    @ManyToOne
    @JoinColumn(name="customerId", insertable = false, updatable = false)
    User customer;
*/

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<LineItem> lineItems;
}
