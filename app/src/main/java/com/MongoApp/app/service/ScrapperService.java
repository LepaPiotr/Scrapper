package com.MongoApp.app.service;

import com.MongoApp.app.entity.Product;
import com.MongoApp.app.entity.ProductPriceList;
import com.MongoApp.app.mongoRepos.ProductRepository;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class ScrapperService {

    private static String URL;

    private ChromeDriver driver;


    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    public void addItem(String name, String shop, Date date, BigDecimal price) {
        String prodId;
        Product prod = productRepository.findByNameIgnoreCaseAndShop(name, shop);
        if(prod != null)
            prodId = prod.getId();
        else{
            prod = productRepository.save(new Product(name, shop,date, price));
        }
        ProductPriceList productPriceList = new ProductPriceList(name, shop, date, price, prod.getId());

        productService.addWitchCheck(productPriceList, prod);
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

    public void scrapeAll(String message) throws InterruptedException {
        try {
            scrapeXKom(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
        scrapeMorele(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
        scrapeEuro(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            // pętla, która przeszukuje strony z wynikami
            while (loop) {
                try {
                    driver.get(driver.getCurrentUrl());
                    waitForLoad(driver);
                    WebElement products = driver.findElement(By.xpath("//div[@id='listing-container']"));
                    prices = (products.findElements(By.xpath("//span[@class='sc-6n68ef-0 sc-6n68ef-3 hNZEsQ']")));
                    names = (products.findElements(By.xpath("//a[@class='sc-1h16fat-0 irSQpN']")));
                    //pętla dodająca dane do bazy dla konkretnej strony
                    for (int i = 0; i < prices.size(); i++) {
                        System.out.println(prices.size() + "rozmiar");
                                      System.out.println(i + ".  " + names.get(i).getText() + " " + prices.get(i).getText());
                        BigDecimal priceBD = new BigDecimal(Float.parseFloat(prices.get(i).getText()
                                .substring(0, prices.get(i).getText().length() - 2)
                                .replace(" ", "")
                                .replace(",", ".")));
                        System.out.println("przed dodawaniem");
                        addItem(names.get(i).getText(), "X-kom", new Date(), priceBD.setScale(2, RoundingMode.HALF_UP));
                        System.out.println("po dodawaniu");
                    }
                    WebElement nextPage = driver.findElement(By.xpath("//div[@class='sc-11oikyw-0 jeEhfJ']\n" +
                            "//a[@class='sc-11oikyw-3 fcPVMJ sc-1h16fat-0 irSQpN']"));
                    nextPage.click();
                } catch (Exception e) {
               //     e.printStackTrace();
                    System.out.println("skończyły się strony X-kom");
                    loop = false;
                }
            }
            //      System.out.println(names.size());
            //     System.out.println(prices.size());
        } catch (Exception e) {
            System.out.println("Błąd X_KOM");
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
                //kliknięcie przycisku akceptującego ciasteczka, który przeszkadza przy dalszych kliknięciach
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
            // pętla, która przeszukuje strony z wynikami, kliknięcie przycisku zmiany strony przed przekierowaniem wywołuje akcję
            // odświerzenia, selenium uznaje, że przekierowanie już wystapiło mimo że tak nie było, stąd musi być sprawdzany warunek
            // czy ścieżka została zmieniona
            while (loop) {
                if (location.equals(driver.getCurrentUrl())) {
                    isLocationChange = false;
                } else {
                    isLocationChange = true;
                    location = driver.getCurrentUrl();
                }
                if (isLocationChange) {
                    try {
                        driver.get(driver.getCurrentUrl());
                        WebElement products = driver.findElement(By.xpath("//div[@class='category-list']"));
                        prices = products.findElements(By.xpath("//div[@class='price-new']"));
                        names = products.findElements(By.xpath("//a[@class='productLink']"));
                        for (int i = 0; i < prices.size(); i++) {
                                            System.out.println(i + ".  " + names.get(i).getText() + " " + prices.get(i).getText());
                            BigDecimal priceBD = new BigDecimal(Float.parseFloat(prices.get(i).getText()
                                    .substring(0, prices.get(i).getText().length() - 2)
                                    .replace(" ", "")
                                    .replace(",", ".")));

                            addItem(names.get(i).getText(), "Morele", new Date(), priceBD.setScale(2, RoundingMode.HALF_UP));
                        }
                        WebElement nextPage = driver.findElement(By.xpath("//li[@class='pagination-lg next']\n" +
                                "//a[@class='pagination-btn']"));
                        nextPage.click();
                        isLocationChange = false;
                    } catch (Exception e) {
                        System.out.println("Skończyły się strony");
                        loop = false;
                    }
                }
            }
        } catch (Exception e) {

            System.out.println("Błąd Morele");
        }
    }

//    public void scrapeMediaExpert(final String value) throws InterruptedException {
//        URL = "https://www.mediaexpert.pl";
//        driver.get(URL);
//        System.out.println(URL + value);
//        waitForLoad(driver);
//        try {
//            final WebElement placeToWrite = driver.findElement(By.xpath("//input[@name='search']"));
//            placeToWrite.sendKeys(value);
//            final WebElement search = driver.findElement(By.xpath("//div[@class='c-search_submit']"));
//            search.click();
//            boolean loop = true;
//            WebElement price;
//            WebElement name;
//
//            while (loop) {
//
//                    try {
//                        driver.get(driver.getCurrentUrl());
//                        WebElement products = driver.findElement(By.xpath("//div[@class='c-grid is-equal is-offers is-lazyLoadContainer is-loaded']"));
//                        List <WebElement> listOfProducts = products.findElements(By.xpath("//div[@class ='c-grid_col is-grid-col-1']"));
//                        int salePriceIndex = 0;
//                        int normalPriceIndex = 0;
//
//                        for(int i = 0; i < listOfProducts.size(); i++){
//                            try{
//                                //próbuje pobrać cene bez promocji
//                                price = listOfProducts.get(i).findElements(By.xpath("//div[@class='c-offerBox_price is-normalPrice is-promoPrice']")).get(normalPriceIndex);
//                                normalPriceIndex ++;
//                            }
//                            catch (Exception e){
//                                //jeśli nie ma ceny bez promocji pobiera cene promocyjną
//                                price = (listOfProducts.get(i).findElements(By.xpath("//div[@class='c-offerBox_price is-normalPrice is-promoPrice']\n" +
//                                        "//span[@class='a-price_price']"))).get(salePriceIndex);
//                                salePriceIndex ++;
//                            }
//                            name = listOfProducts.get(i).findElements(By.xpath("//h2[@class='c-offerBox_data ']\n" +
//                                    "//a[@class='a-typo is-secondary']")).get(i);
//
//                            System.out.println(name.getText() + " " + price.getText().substring(0,price.getText().length() - 2)
//                                    .replace(" ", "").replace("\n",""));
//                            BigDecimal priceBD = new BigDecimal(Float.parseFloat(price.getText().substring(0,price.getText().length() - 2)
//                                    .replace(" ", "").replace("\n","")));
//                            Product productToAdd = new Product(name.getText(), "MediaExpert", new Date(),
//                                    priceBD.setScale(2, RoundingMode.HALF_UP));
//                            addItem(productToAdd);
//                        }
//                        WebElement nextPage = driver.findElement(By.xpath("//div[@class='c-toolbar ']\n" +
//                                "//a[@class='is-nextLink']"));
//                        nextPage.click();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        loop = false;
//                    }
//                }
//        } catch (Exception e) {
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
            List<WebElement> prices = new ArrayList<>();
            List<WebElement> names = new ArrayList<>();
            boolean loop = true;
            String location = "";
            boolean isLocationChange = true;
            // pętla, która przeszukuje strony z wynikami
            while (loop) {
                if (location.equals(driver.getCurrentUrl())) {
                    isLocationChange = false;
                } else {
                    isLocationChange = true;
                    location = driver.getCurrentUrl();
                }
                if (isLocationChange) {
                    try {
                        System.out.println(driver.getCurrentUrl());
                        driver.get(driver.getCurrentUrl());
                        waitForLoad(driver);
                        WebElement products = driver.findElement(By.xpath("//div[@id='products']"));
                        prices = (products.findElements(By.xpath("//div[@class='price-normal selenium-price-normal']")));
                        names = (products.findElements(By.xpath("//a[@class='js-save-keyword']")));
                        //pętla dodająca dane do bazy dla konkretnej strony
                        for (int i = 1; i < names.size(); i += 2) {
                        System.out.println("jestem w pętli");

                        System.out.println(i + ".  " + names.get(i).getText() + " " + prices.get(i).getText());

                        BigDecimal priceBD = new BigDecimal(Float.parseFloat(prices.get(i).getText()
                                .substring(0, prices.get(i).getText().length() - 2)
                                .replace(" ", "")
                                .replace(",", ".")));

                            addItem(names.get(i).getText(), "Euro", new Date(), priceBD.setScale(2, RoundingMode.HALF_UP));
                        }
                        WebElement nextPage = driver.findElement(By.xpath("//a[@class = 'paging-next selenium-WC-paging-next-button']"));
                        nextPage.click();
                    } catch (Exception e) {
                        System.out.println("Skończyły się strony");
                        loop = false;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Błąd Euro");
        }
    }
}
