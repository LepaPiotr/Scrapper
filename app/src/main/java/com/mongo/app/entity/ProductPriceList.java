package com.mongo.app.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ProductPriceList {
    @Id
    private String id;
    private String name;
    private String shop;
    private Date dateOfAdd;
    private BigDecimal price;
    private String productId;

    public ProductPriceList(String name, String shop, Date dateOfAdd, BigDecimal price, String productId) {
        this.productId = productId;
        this.name = name;
        this.shop = shop;
        this.dateOfAdd = dateOfAdd;
        this.price = price;
    }
}
