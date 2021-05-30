package com.MongoApp.app.mongoRepos;

import com.MongoApp.app.entity.ProductPriceList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPriceListRepository extends MongoRepository<ProductPriceList, String>  {

    public List<ProductPriceList> findByNameLikeIgnoreCase(String Name);
    public List<ProductPriceList> findByShopLikeIgnoreCase(String shop);
    public ProductPriceList findTop1ByNameAndShopIgnoreCaseOrderByDateOfAddDesc(String Name, String Shop);
    public List<ProductPriceList> findByProductId(String id);

    //public ProductPriceList deleteById (int prodId);


}
