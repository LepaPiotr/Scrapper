package com.MongoApp.app.service;

import com.MongoApp.app.entity.Product;
import com.MongoApp.app.mongoRepos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<Product> doSomeActions(){
        productRepository.deleteAll();
        productRepository.save(new Product("Redmi 7", "Xiaomi", new Date(), 799.99f));
        productRepository.save(new Product("S21", "Samsung", new Date() , 5699.99f));
        return productRepository.findAll();
    }



}
