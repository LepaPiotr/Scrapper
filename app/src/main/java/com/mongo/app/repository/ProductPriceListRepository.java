package com.mongo.app.repository;

import com.mongo.app.entity.ProductPriceList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPriceListRepository extends MongoRepository<ProductPriceList, String> {

    List<ProductPriceList> findByNameLikeIgnoreCase(String name);

    List<ProductPriceList> findByShopLikeIgnoreCase(String shop);

    ProductPriceList findTop1ByNameAndShopIgnoreCaseOrderByDateOfAddDesc(String name, String shop);

    List<ProductPriceList> findByProductId(String id);
}
