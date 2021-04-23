package com.MongoApp.app.controller;

import com.MongoApp.app.entity.Product;
import com.MongoApp.app.entity.ProductPriceList;
import com.MongoApp.app.mongoRepos.ProductPriceListRepository;
import com.MongoApp.app.mongoRepos.ProductRepository;
import com.MongoApp.app.service.ProductService;
import com.MongoApp.app.service.ScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductRESTController {

    @Autowired
    ProductRepository repository;


    @GetMapping()
    public List<Product> findAll(){
        return repository.findAll();
    }


    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") String prodId) {
        repository.deleteById(prodId);
    }

}
