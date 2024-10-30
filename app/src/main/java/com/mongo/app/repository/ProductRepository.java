package com.mongo.app.repository;

import com.mongo.app.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Product findByNameIgnoreCaseAndShop(String name, String shop);

    List<Product> findByNameLikeIgnoreCase(String name);


}
