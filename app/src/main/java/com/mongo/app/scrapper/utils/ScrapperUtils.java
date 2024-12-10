package com.mongo.app.scrapper.utils;

import com.mongo.app.configuration.SeleniumConfiguration;
import com.mongo.app.entity.Product;
import com.mongo.app.entity.ProductPriceList;
import com.mongo.app.repository.ProductRepository;
import com.mongo.app.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.Date;
@UtilityClass
@Slf4j
public class ScrapperUtils {

    private ProductService productService;
    private ProductRepository productRepository;
    private SeleniumConfiguration seleniumConfiguration;

    public void addItem(String name, String shop, Date date, BigDecimal price, String link) {
        Product prod = productRepository.findByNameIgnoreCaseAndShop(name, shop);
        if (prod == null) {
            prod = productRepository.save(new Product(name, shop, date, price, link));
        }
        ProductPriceList productPriceList = new ProductPriceList(name, shop, date, price, prod.getId());
        productService.addWitchCheck(productPriceList, prod, date);
    }

    public WebElement findElement(String path){
        return seleniumConfiguration.driver().findElement(By.xpath(path));
    }

    public void searchPhrase(String searchBarPath, String searchButtonPath, String value){
        final WebElement serachBar = findElement(searchBarPath);
        serachBar.sendKeys(value);
        final WebElement searchButton = findElement(searchButtonPath);
        searchButton.click();
    }

    public void getToPage(String value){
        seleniumConfiguration.driver().get(value);
    }

    public BigDecimal getPrice (String price){
        return BigDecimal.valueOf(Float.parseFloat(price
                .substring(0, price.length() - 2)
                .replace(" ", "")
                .replace(",", ".")));
    }

    public void acceptCokies(String target){
        try {
            final WebElement cookie = ScrapperUtils.findElement(target);
            cookie.click();
        } finally {
            log.info("Brak przycisku");
        }
    }
}
