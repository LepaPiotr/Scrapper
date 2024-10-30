package com.mongo.app.controller;

import com.mongo.app.entity.Product;
import com.mongo.app.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin
@AllArgsConstructor
public class ProductRESTController {

    ProductService productService;

    @GetMapping()
    public List<Product> findAll() {
        return productService.findAllProducts();
    }

    @GetMapping("/name/{name}")
    public List<Product> findByName(@PathVariable("name") String name) {
        return productService.findProductByName(name);
    }

}
