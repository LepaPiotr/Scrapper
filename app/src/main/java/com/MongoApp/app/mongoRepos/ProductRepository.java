package com.MongoApp.app.mongoRepos;

import com.MongoApp.app.entity.Product;
import com.MongoApp.app.entity.ProductPriceList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>  {

    public Product findByNameIgnoreCaseAndShop(String Name, String Shop);
    public List<Product> findByShopLikeIgnoreCase(String shop);
    public Product findTop1ByNameAndShopIgnoreCaseOrderByDateOfActualizationDesc(String Name, String Shop);
    public Product save (Product productPriceList);
    public Product deleteById (int prodId);


}
