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
    private Date dateOfAdd;
    private BigDecimal price;

    public Product() {
    }

    public Product(String name, String shop, Date dateOfAdd, BigDecimal price) {
        this.name = name;
        this.dateOfAdd = dateOfAdd;
        this.price = price;
        this.shop = shop;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) &&
                name.equals(product.name) &&
                shop.equals(product.shop) &&
                dateOfAdd.equals(product.dateOfAdd) &&
                price.equals(product.price);
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


    public Date getDateOfAdd() {
        return dateOfAdd;
    }

    public void setDateOfAdd(Date dateOfAdd) {
        this.dateOfAdd = dateOfAdd;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
