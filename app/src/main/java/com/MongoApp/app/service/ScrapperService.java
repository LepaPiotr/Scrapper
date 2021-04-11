package com.MongoApp.app.service;

import com.MongoApp.app.entity.Product;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@Service
@AllArgsConstructor
public class ScrapperService {

    private static String URL;

    private ChromeDriver driver;

    private static int counter = 0;

    @Autowired
    private ProductService productService;

    public void addItem(Product product){
        productService.addWitchCheck(product);
    }

    public void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        new WebDriverWait(driver, 30).until(pageLoadCondition);
    }
    public void scrapeXKom(final String value) {
        URL = "https://www.x-kom.pl";
        driver.get(URL);
    //    System.out.println(URL + value);
        waitForLoad(driver);
        try {
            final WebElement placeToWrite = driver.findElement(By.xpath("//input[@class='sc-1hdf4hr-0 frAjNp']"));
            placeToWrite.sendKeys(value);
            final WebElement search = driver.findElement(By.xpath("//button[@class='sc-15gi9e9-7 apKoa']"));
            search.click();
            List<WebElement> prices = new ArrayList<>();
            List<WebElement> names = new ArrayList<>();
            boolean loop = true;
            while (loop) {
                try {
                    driver.get(driver.getCurrentUrl());
                    waitForLoad(driver);
                    WebElement products = driver.findElement(By.xpath("//div[@id='listing-container']"));
                    prices = (products.findElements(By.xpath("//span[@class='sc-6n68ef-0 sc-6n68ef-3 hNZEsQ']")));
                    names = (products.findElements(By.xpath("//a[@class='sc-1h16fat-0 irSQpN']")));
                    for (int i = 0; i < prices.size(); i++) {
          //              System.out.println(i + ".  " + names.get(i).getText() + " " + prices.get(i).getText());
                        BigDecimal priceBD = new BigDecimal(Float.parseFloat(prices.get(i).getText()
                                .substring(0, prices.get(i).getText().length() -2)
                                .replace(" ", "")
                                .replace(",",".")));

                        Product productToAdd = new Product(names.get(i).getText(), "X-kom", new Date(),
                                priceBD.setScale(2, RoundingMode.HALF_UP));
                        addItem(productToAdd);
                    }
                    WebElement nextPage = driver.findElement(By.xpath("//div[@class='sc-11oikyw-0 jeEhfJ']\n" +
                            "//a[@class='sc-11oikyw-3 fcPVMJ sc-1h16fat-0 irSQpN']"));
                    nextPage.click();
                } catch (Exception e) {
         //           System.out.println("skończyły się strony");
                    loop = false;
                }
            }
      //      System.out.println(names.size());
       //     System.out.println(prices.size());
            counter = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scrapeMorele(final String value) throws InterruptedException {
        URL = "https://www.morele.net/";
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(URL);
     //   System.out.println(URL + value);
        waitForLoad(driver);
        try {
            try {
                final WebElement cookie = driver.findElement(By.xpath("//button[@class='btn btn-secondary btn-secondary-outline btn-md close-cookie-box']"));
                cookie.click();
            } catch (Exception e) {
                System.out.println("nie ma przycisku casteczek");
            }

            final WebElement placeToWrite = driver.findElement(By.xpath("//input[@name='search']"));
            placeToWrite.sendKeys(value);
            final WebElement search = driver.findElement(By.xpath("//button[@class='btn btn-primary h-quick-search-submit']"));
            search.click();
            List<WebElement> prices = new ArrayList<>();
            List<WebElement> names = new ArrayList<>();
            boolean loop = true;
            String location = "";
            boolean isLocationChange = true;
            while (loop) {
         //       System.out.println("Wykonuje się w pętli i powinienem wymusić kolejną stronę");
                if(location.equals(driver.getCurrentUrl())){
                    isLocationChange = false;
                }
                else {
                    isLocationChange = true;
                    location = driver.getCurrentUrl();
                }
                if(isLocationChange) {
                    try {
                        driver.get(driver.getCurrentUrl());
                        WebElement products = driver.findElement(By.xpath("//div[@class='category-list']"));
                        prices = products.findElements(By.xpath("//div[@class='price-new']"));
                        names = products.findElements(By.xpath("//a[@class='productLink']"));
                        for (int i = 0; i < prices.size(); i++) {
            //                System.out.println(i + ".  " + names.get(i).getText() + " " + prices.get(i).getText());
                            BigDecimal priceBD = new BigDecimal(Float.parseFloat(prices.get(i).getText()
                                    .substring(0, prices.get(i).getText().length() -2)
                                    .replace(" ", "")
                                    .replace(",",".")));
                            Product productToAdd = new Product(names.get(i).getText(), "Morele", new Date(),
                                    priceBD.setScale(2, RoundingMode.HALF_UP));
                            addItem(productToAdd);
                        }
                        WebElement nextPage = driver.findElement(By.xpath("//li[@class='pagination-lg next']\n" +
                                "//a[@class='pagination-btn']"));
                        nextPage.click();
                        isLocationChange = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                        loop = false;
                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void scrapeMediaExpert(final String value) throws InterruptedException {
        URL = "https://www.mediaexpert.pl";
        driver.get(URL);
        System.out.println(URL + value);
        waitForLoad(driver);
        try {
            final WebElement placeToWrite = driver.findElement(By.xpath("//input[@name='search']"));
            placeToWrite.sendKeys(value);
            final WebElement search = driver.findElement(By.xpath("//div[@class='c-search_submit']"));
            search.click();
            driver.get(driver.getCurrentUrl());
            waitForLoad(driver);
            final WebElement products = driver.findElement(By.xpath("//div[@class='c-grid is-equal is-offers is-lazyLoadContainer is-loaded']"));
            List<WebElement> prices = products.findElements(By.xpath("//div[@class='c-offerBox_price is-normalPrice is-promoPrice']\n" +
                    "//span[@class='a-price_price']"));
            prices.addAll(products.findElements(By.xpath("//div[@class='c-offerBox_price is-normalPrice ']\n" +
                    "                //span[@class='a-price_price']")));
            final List<WebElement> names = products.findElements(By.xpath("//h2[@class='c-offerBox_data ']\n" +
                    "//a[@class='a-typo is-secondary']"));
            System.out.println(names.size());
            System.out.println(prices.size());
            counter = 0;
            for (int i = 0; i < prices.size(); i++) {
                System.out.println(i + ".  " + names.get(i).getText() + " " + prices.get(i).getText());
            }
        } catch (Exception e) {
            counter++;
            if (counter < 3) {
                scrapeMediaExpert(value);
            }
            e.printStackTrace();
        }
    }
//    public void scrapeMediaMarkt(final String value) throws InterruptedException {
//        URL = "https://mediamarkt.pl/";
//        driver.get(URL);
//        System.out.println(URL + value);
//        waitForLoad(driver);
//        WebDriverWait wait = new WebDriverWait(driver, 30);
//        try{
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='m-search_input js-search-input js-search_input']")));driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
//        final WebElement placeToWrite = driver.findElement(By.xpath("//input[@class='m-search_input js-search-input js-search_input']"));
//        placeToWrite.sendKeys(value);
//        final WebElement search = driver.findElement(By.xpath("//button[@id='js-triggerSearch']"));
//        search.click();
//        driver.get(driver.getCurrentUrl());
//        waitForLoad(driver);
//        final WebElement products = driver.findElement(By.xpath("//div[@class='b-row clearfix2 b-listing_classic js-eqContainer js-offerBox js-equalHRow']"));
//        final List<WebElement> prices = products.findElements(By.xpath("//div[@itemprop='price']"));
//        final List<WebElement> names = products.findElements(By.xpath("//a[@class='b-ofr_headDataTitle']"));
//        System.out.println(names.size());
//        System.out.println(prices.size());
//            counter = 0;
//        for (int i = 0; i < prices.size(); i++) {
//            System.out.println(i + ".  " + names.get(i).getText() + " " + prices.get(i).getText());
//        }
//        } catch (Exception e) {
//            counter ++;
//            if(counter < 3) {
//                scrapeMediaMarkt(value);
//            }
//            e.printStackTrace();
//        }
//    }

    public void scrapeEuro(final String value) throws InterruptedException {
        URL = "https://www.euro.com.pl/";
        driver.get(URL);
        System.out.println(URL + value);
        waitForLoad(driver);
        try {
            final WebElement placeToWrite = driver.findElement(By.xpath("//input[@id='keyword']"));
            placeToWrite.sendKeys(value);
            final WebElement search = driver.findElement(By.xpath("//a[@class='selenium-search-button']"));
            search.click();
            driver.get(driver.getCurrentUrl());
            waitForLoad(driver);
            final WebElement products = driver.findElement(By.xpath("//div[@id='products']"));
            final List<WebElement> prices = products.findElements(By.xpath("//div[@class='price-normal selenium-price-normal']"));
            final List<WebElement> names = products.findElements(By.xpath("//a[@class='js-save-keyword']"));
            System.out.println(names.size());
            System.out.println(prices.size());
            counter = 0;
            for (int i = 1; i < prices.size(); i += 2) {
                System.out.println(i + ".  " + names.get(i).getText() + " " + prices.get(i).getText());
            }
        } catch (Exception e) {
            counter++;
            if (counter < 3) {
                scrapeEuro(value);
            }
            e.printStackTrace();
        }
    }


}
