package com.mongo.app.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Product {
    @Id
    private String id;
    private String name;
    private String shop;
    private Date dateOfActualization;
    private BigDecimal lastKnowPrice;
    private String link;

    public Product(String name, String shop, Date dateOfActualization, BigDecimal lastKnowPrice, String link) {
        this.name = name;
        this.shop = shop;
        this.dateOfActualization = dateOfActualization;
        this.lastKnowPrice = lastKnowPrice;
        this.link = link;
    }
}
