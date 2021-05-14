package com.MongoApp.app.entity;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Product {
    @Id
    private String id;
    private String name;
    private String shop;
    private Date dateOfActualization;
    private BigDecimal lastKnowPrice;
    private String link;

    public Product() {
    }

    public Product(String name, String shop, Date dateOfActualization, BigDecimal lastKnowPrice, String link) {
        this.name = name;
        this.dateOfActualization = dateOfActualization;
        this.lastKnowPrice = lastKnowPrice;
        this.shop = shop;
        this.link = link;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product productPriceList = (Product) o;
        return id.equals(productPriceList.id) &&
                name.equals(productPriceList.name) &&
                shop.equals(productPriceList.shop) &&
                dateOfActualization.equals(productPriceList.dateOfActualization) &&
                lastKnowPrice.equals(productPriceList.lastKnowPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Date getDateOfActualization() {
        return dateOfActualization;
    }

    public void setDateOfActualization(Date dateOfAdd) {
        this.dateOfActualization = dateOfAdd;
    }

    public BigDecimal getLastKnowPrice() {
        return lastKnowPrice;
    }

    public void setLastKnowPrice(BigDecimal price) {
        this.lastKnowPrice = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
