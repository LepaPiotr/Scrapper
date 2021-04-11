package com.MongoApp.app.mongoRepos;

import com.MongoApp.app.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>  {

    public List<Product> findByNameLikeIgnoreCase(String Name);
    public List<Product> findByShopLikeIgnoreCase(String shop);
    public Product findTop1ByNameAndShopIgnoreCaseOrderByDateOfAddDesc(String Name, String Shop);
    public Product save (Product product);
    public Product deleteById (int prodId);


}
