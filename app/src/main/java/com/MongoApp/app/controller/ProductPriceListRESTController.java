package com.MongoApp.app.controller;

import com.MongoApp.app.entity.ProductPriceList;
import com.MongoApp.app.mongoRepos.ProductPriceListRepository;
import com.MongoApp.app.service.ProductService;
import com.MongoApp.app.service.ScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productPriceList")
@CrossOrigin
public class ProductPriceListRESTController {

    @Autowired
    ProductPriceListRepository repository;

    @Autowired
    ProductService productService;

    @Autowired
    ScrapperService scrapperService;

    @GetMapping("/name/{name}")
    public List<ProductPriceList> findByName(@PathVariable("name") String name){
        return repository.findByNameLikeIgnoreCase(name);
    }

    @GetMapping("/id/{id}")
    public List<ProductPriceList> findById(@PathVariable("id") String id){
        return repository.findByProductId(id);
    }

    @GetMapping("/nameTop/{name}?{shop}")
    public ProductPriceList findByNameTop(@PathVariable("name") String name, @PathVariable("name") String shop){
        return repository.findTop1ByNameAndShopIgnoreCaseOrderByDateOfAddDesc(name, shop);
    }

    @GetMapping("/man/{manufacturer}")
    public List<ProductPriceList> findByManufacturer(@PathVariable("manufacturer") String manufacturer){
        return repository.findByShopLikeIgnoreCase(manufacturer);
    }

    @GetMapping()
    public List<ProductPriceList> findAll(){
        return repository.findAll();
    }

    @GetMapping("/Test/{name}")
    public void seleniumFind(@PathVariable("name") String name) throws InterruptedException {
       scrapperService.scrapeXKom(name);
       scrapperService.scrapeMorele(name);
       scrapperService.scrapeEuro(name);
    }

    @PostMapping
    public ProductPriceList createCustomer(@RequestBody ProductPriceList productPriceList) {
        return repository.save(productPriceList);
    }

    @PutMapping
    public ProductPriceList updateCustomer(@RequestBody ProductPriceList productPriceList) {
        return repository.save(productPriceList);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") String prodId) {
        repository.deleteById(prodId);
    }

}
