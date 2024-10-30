package com.mongo.app.service;

import com.mongo.app.entity.ProductPriceList;
import com.mongo.app.repository.ProductPriceListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductPriceListService {
    ProductPriceListRepository productPriceListRepository;

    public List<ProductPriceList> findProductPriceByName(String name) {
        return productPriceListRepository.findByNameLikeIgnoreCase(name);
    }

    public List<ProductPriceList> findAllProductPrice() {
        return productPriceListRepository.findAll();
    }

    public List<ProductPriceList> findByProductId(String id) {
        return productPriceListRepository.findByProductId(id);
    }


}
