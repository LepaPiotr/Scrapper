package com.mongo.app.controller;

import com.mongo.app.entity.ProductPriceList;
import com.mongo.app.service.ProductPriceListService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productPriceList")
@CrossOrigin
@AllArgsConstructor
public class ProductPriceListRESTController {

    ProductPriceListService productPriceListService;


    @GetMapping("/name/{name}")
    public List<ProductPriceList> findByName(@PathVariable("name") String name){
        return productPriceListService.findProductPriceByName(name);
    }

    @GetMapping("/id/{id}")
    public List<ProductPriceList> findById(@PathVariable("id") String id){
        return productPriceListService.findByProductId(id);
    }


    @GetMapping()
    public List<ProductPriceList> findAll(){
        return productPriceListService.findAllProductPrice();
    }



}
