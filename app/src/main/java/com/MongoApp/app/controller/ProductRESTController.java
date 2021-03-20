package com.MongoApp.app.controller;

import com.MongoApp.app.entity.Product;
import com.MongoApp.app.mongoRepos.ProductRepository;
import com.MongoApp.app.service.ProductService;
import com.MongoApp.app.service.ScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductRESTController {

    @Autowired
    ProductRepository repository;

    @Autowired
    ProductService productService;

    @Autowired
    ScrapperService scrapperService;

    @GetMapping("/name/{name}")
    public List<Product> findByName(@PathVariable("name") String name){
        return repository.findByNameLikeIgnoreCase(name);
    }

    @GetMapping("/man/{manufacturer}")
    public List<Product> findByManufacturer(@PathVariable("manufacturer") String manufacturer){
        return repository.findByManufacturerLikeIgnoreCase(manufacturer);
    }

    @GetMapping()
    public List<Product> findAll(){
        return repository.findAll();
    }

    @GetMapping("/Test/{name}")
    public void seleniumFind(@PathVariable("name") String name) throws InterruptedException {
       scrapperService.scrapeXKom(name);
       scrapperService.scrapeMorele(name);
       scrapperService.scrapeMediaExpert(name);
       scrapperService.scrapeMediaMarkt(name);
       scrapperService.scrapeEuro(name);


    }

    @PostMapping("/addCon")
    public void  createConstraint(@RequestBody Product product) {
        productService.doSomeActions();
    }

    @PostMapping
    public Product createCustomer(@RequestBody Product product) {
        return repository.save(product);
    }

    @PutMapping
    public Product updateCustomer(@RequestBody Product product) {
        return repository.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") String prodId) {
        repository.deleteById(prodId);
    }

}
