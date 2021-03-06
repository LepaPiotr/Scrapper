package com.MongoApp.app.mongoRepos;

import com.MongoApp.app.entity.Product;
import com.MongoApp.app.entity.ProductPriceList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>  {

    public Product findByNameIgnoreCaseAndShop(String Name, String Shop);
    public List<Product> findByNameLikeIgnoreCase(String Name);


}
