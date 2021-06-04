package com.MongoApp.app;

import com.MongoApp.app.entity.Product;
import com.MongoApp.app.entity.ProductPriceList;
import com.MongoApp.app.mongoRepos.ProductPriceListRepository;
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
public class ProductPriceListTest {

    @Autowired
    ProductPriceListRepository productPriceListRepository;


    @Test
    @Order(1)
    void addProductPriceList() {
        ProductPriceList productPriceList = new ProductPriceList("Test", "Test", new Date(), BigDecimal.ONE, "Test");
        productPriceListRepository.save(productPriceList);
        List<ProductPriceList> test = productPriceListRepository.findByProductId("Test");
        if (test != null && !test.isEmpty()) {
            assert true;
        } else {
            assert false;
        }
    }

    @Test
    @Order(2)
    void modProductPriceList() {
        List<ProductPriceList> test = productPriceListRepository.findByProductId("Test");
        boolean isGood = false;
        if (test != null && !test.isEmpty()) {
            ProductPriceList prod = new ProductPriceList(test.get(0).getName(), test.get(0).getShop(),
                    test.get(0).getDateOfAdd(), test.get(0).getPrice(), "newId");
            prod.setId(test.get(0).getId());
            productPriceListRepository.save(prod);
        }
        List<ProductPriceList> test2 = productPriceListRepository.findByProductId("newId");
        if (test2 != null && !test2.isEmpty() && test.size() == test2.size()) {
            for (ProductPriceList singleTest : test2) {
                System.out.println(singleTest.getProductId().trim());
                if (singleTest.getProductId().trim().equals("newId")) {
                    isGood = true;
                }
            }
        }
        assert isGood;
    }

    @Test
    @Order(3)
    void deleteProductPriceList() {
        List<ProductPriceList> test = productPriceListRepository.findByProductId("newId");
        if (test != null && !test.isEmpty()) {
            for(ProductPriceList singleProduct: test) {
                productPriceListRepository.delete(singleProduct);
            }
        }
        else{
            assert false;
        }
        List<ProductPriceList> test2 = productPriceListRepository.findByProductId("newId");
        if (test2 == null || test2.isEmpty())
            assert true;
        else
            assert false;

    }


}
