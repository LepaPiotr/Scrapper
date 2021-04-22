package com.MongoApp.app.entity;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class ProductPriceList {
    @Id
    private String id;
    private String productId;
    private String name;
    private String shop;
    private Date dateOfAdd;
    private BigDecimal price;

    public ProductPriceList() {
    }

    public ProductPriceList(String name, String shop, Date dateOfAdd, BigDecimal price, String productId) {
        this.name = name;
        this.dateOfAdd = dateOfAdd;
        this.price = price;
        this.shop = shop;
        this.productId = productId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPriceList productPriceList = (ProductPriceList) o;
        return id.equals(productPriceList.id) &&
                name.equals(productPriceList.name) &&
                shop.equals(productPriceList.shop) &&
                dateOfAdd.equals(productPriceList.dateOfAdd) &&
                price.equals(productPriceList.price);
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
