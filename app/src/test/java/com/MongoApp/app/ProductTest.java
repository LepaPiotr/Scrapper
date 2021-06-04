package com.MongoApp.app;

import com.MongoApp.app.entity.Product;
import com.MongoApp.app.mongoRepos.ProductRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTest {

    @Autowired
    ProductRepository productRepository;


    @Test
    @Order(1)
    void addProduct() {
        Product product = new Product("Test", "Test", new Date(), BigDecimal.ONE, "");
        productRepository.save(product);
        List<Product> test = productRepository.findByNameLikeIgnoreCase("Test");
        if (test != null && !test.isEmpty()) {
            assert true;
        } else {
            assert false;
        }
    }

    @Test
    @Order(2)
    void modProduct() {
        List<Product> test = productRepository.findByNameLikeIgnoreCase("Test");
        boolean isGood = false;
        if (test != null && !test.isEmpty()) {
            Product prod = new Product(test.get(0).getName(), test.get(0).getShop(),
                    test.get(0).getDateOfActualization(), test.get(0).getLastKnowPrice(), "newLink");
            prod.setId(test.get(0).getId());
            productRepository.save(prod);
        }
        List<Product> test2 = productRepository.findByNameLikeIgnoreCase("Test");
        if (test2 != null && !test2.isEmpty() && test.size() == test2.size()) {
            for (Product singleTest : test2) {
                System.out.println(singleTest.getLink().trim());
                if (singleTest.getLink().trim().equals("newLink")) {
                    isGood = true;
                }
            }
        }
        assert isGood;
    }

    @Test
    @Order(3)
    void deleteProduct() {
        List<Product> test = productRepository.findByNameLikeIgnoreCase("Test");
        if (test != null && !test.isEmpty()) {
            for(Product singleProduct: test) {
                productRepository.delete(singleProduct);
            }
        }
        else{
            assert false;
        }
        List<Product> test2 = productRepository.findByNameLikeIgnoreCase("Test");
        if (test2 == null || test2.isEmpty())
            assert true;
        else
            assert false;

    }


}
