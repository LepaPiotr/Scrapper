package com.MongoApp.app.entity;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Objects;

public class Product {
    @Id
    private String id;

    private String name;
    private String manufacturer;
    private Date dateOdAdd;
    private float price;

    public Product() {
    }

    public Product(String name, String manufacturer, Date dateOdAdd, float price) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.dateOdAdd = dateOdAdd;
        this.price = price;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(manufacturer, product.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Date getDateOdAdd() {
        return dateOdAdd;
    }

    public void setDateOdAdd(Date dateOdAdd) {
        this.dateOdAdd = dateOdAdd;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


}
