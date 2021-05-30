package com.MongoApp.app.controller;

import com.MongoApp.app.entity.ProductPriceList;
import com.MongoApp.app.mongoRepos.ProductPriceListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productPriceList")
@CrossOrigin
public class ProductPriceListRESTController {

    @Autowired
    ProductPriceListRepository repository;


    @GetMapping("/name/{name}")
    public List<ProductPriceList> findByName(@PathVariable("name") String name){
        return repository.findByNameLikeIgnoreCase(name);
    }

    @GetMapping("/id/{id}")
    public List<ProductPriceList> findById(@PathVariable("id") String id){
        return repository.findByProductId(id);
    }


    @GetMapping()
    public List<ProductPriceList> findAll(){
        return repository.findAll();
    }



}
