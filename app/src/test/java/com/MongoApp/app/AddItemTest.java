package com.MongoApp.app;

import com.MongoApp.app.entity.Product;
import com.MongoApp.app.entity.ProductPriceList;
import com.MongoApp.app.mongoRepos.ProductPriceListRepository;
import com.MongoApp.app.mongoRepos.ProductRepository;
import com.MongoApp.app.service.ScrapperService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AddItemTest {

    @Autowired
    ScrapperService scrapperService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPriceListRepository productPriceListRepository;

    @Test
    public void AddItemTest(){
        scrapperService.addItem("Test" , "Test", new Date(), BigDecimal.ONE, "");
        List<Product> productsList = productRepository.findByNameLikeIgnoreCase("Test");
        List<ProductPriceList> productsPriceList = productPriceListRepository.findByProductId(productsList.get(0).getId());
        assert productsList.size() == 1 || productsPriceList.size() == 1;

        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);

        scrapperService.addItem("Test" , "Test", cal.getTime(), BigDecimal.ONE, "");

        productsList = productRepository.findByNameLikeIgnoreCase("Test");
        productsPriceList = productPriceListRepository.findByProductId(productsList.get(0).getId());

        if(productsList.size() != 1 || productsPriceList.size() != 1 || !productsPriceList.get(0).getDateOfAdd().equals(cal.getTime()))
            assert false;

        scrapperService.addItem("Test" , "Test", cal.getTime(), BigDecimal.TEN, "");

        productsList = productRepository.findByNameLikeIgnoreCase("Test");
        productsPriceList = productPriceListRepository.findByProductId(productsList.get(0).getId());

        if(productsList.size() != 1 || productsPriceList.size() != 2 )
            assert false;
    }
}
