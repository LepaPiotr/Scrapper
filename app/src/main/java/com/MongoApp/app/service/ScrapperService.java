package com.MongoApp.app.service;

import com.MongoApp.app.configuration.SeleniumConfiguration;
import com.MongoApp.app.entity.Product;
import com.MongoApp.app.entity.ProductPriceList;
import com.MongoApp.app.mongoRepos.ProductRepository;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SeleniumConfiguration seleniumConfiguration;

    public void addItem(String name, String shop, Date date, BigDecimal price, String link) {
        String prodId;
        Product prod = productRepository.findByNameIgnoreCaseAndShop(name, shop);
        if (prod != null)
            prodId = prod.getId();
        else {
            prod = productRepository.save(new Product(name, shop, date, price, link));
        }
        ProductPriceList productPriceList = new ProductPriceList(name, shop, date, price, prod.getId());
        productService.addWitchCheck(productPriceList, prod);
    }

    public void scrapeAll(String message) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                scrapeXKom(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                scrapeMorele(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread thread3 = new Thread(() -> {
            try {
                scrapeEuro(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join(30000);
        thread2.join(30000);
        thread3.join(30000);
    }

    public void scrapeXKom(final String value) {

        ChromeDriver driver = seleniumConfiguration.driver();

        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        URL = "https://www.x-kom.pl";
        driver.get(URL);
        try {
            final WebElement placeToWrite = driver.findElement(By.xpath("//input[@class='sc-1hdf4hr-0 frAjNp']"));
            placeToWrite.sendKeys(value);
            final WebElement search = driver.findElement(By.xpath("//button[@class='sc-15gi9e9-7 apKoa']"));
            search.click();
            List<WebElement> prices;
            List<WebElement> names;
            boolean loop = true;
            // pętla, która przeszukuje strony z wynikami
            int counter = 1;
            while (loop) {
                if (counter > 10) {
                    break;
                }
                try {
                    counter++;
                    driver.get(driver.getCurrentUrl());
                    WebElement products = driver.findElement(By.xpath("//div[@id='listing-container']"));
                    prices = (products.findElements(By.xpath("//span[@class='sc-6n68ef-0 sc-6n68ef-3 hNZEsQ']")));
                    names = (products.findElements(By.xpath("//a[@class='sc-1h16fat-0 irSQpN']")));
                    //pętla dodająca dane do bazy dla konkretnej strony
                    for (int i = 0; i < prices.size(); i++) {
                        BigDecimal priceBD = BigDecimal.valueOf(Float.parseFloat(prices.get(i).getText()
                                .substring(0, prices.get(i).getText().length() - 2)
                                .replace(" ", "")
                                .replace(",", ".")));
                        addItem(names.get(i).getText(), "X-kom", new Date(), priceBD.setScale(2, RoundingMode.HALF_UP),
                                names.get(i).getAttribute("href"));
                    }
                    WebElement nextPage = driver.findElement(By.xpath("//div[@class='sc-11oikyw-0 jeEhfJ']\n" +
                            "//a[@class='sc-11oikyw-3 fcPVMJ sc-1h16fat-0 irSQpN']"));
                    nextPage.click();
                } catch (Exception e) {
                    System.out.println("end of pages");
                    loop = false;
                }
            }
        } catch (Exception e) {
            driver.close();
        }
        driver.close();
    }

    public void scrapeMorele(final String value) throws InterruptedException {
        ChromeDriver driver = seleniumConfiguration.driver();

        URL = "https://www.morele.net/";
        driver.get(URL);
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
            List<WebElement> prices;
            List<WebElement> names;
            boolean loop = true;
            String location = "";
            boolean isLocationChange = true;
            // pętla, która przeszukuje strony z wynikami, kliknięcie przycisku zmiany strony przed przekierowaniem wywołuje akcję
            // odświerzenia, selenium uznaje, że przekierowanie już wystapiło mimo że tak nie było, stąd musi być sprawdzany warunek
            // czy ścieżka została zmieniona
            int pageCounter = 1;
            long errorCounter =System.currentTimeMillis();
            while (loop) {
                if (pageCounter > 10) {
                    break;
                }
                if (location.equals(driver.getCurrentUrl())) {
                    if (errorCounter + (20000) < System.currentTimeMillis()) {
                        break;
                    }
                    isLocationChange = false;
                } else {
                    isLocationChange = true;
                    errorCounter = 0;
                    location = driver.getCurrentUrl();
                }
                if (isLocationChange) {
                    try {
                        pageCounter++;
                        driver.get(driver.getCurrentUrl());
                        WebElement products = driver.findElement(By.xpath("//div[@class='category-list']"));
                        prices = products.findElements(By.xpath("//div[@class='price-new']"));
                        names = products.findElements(By.xpath("//a[@class='productLink']"));

                        for (int i = 0; i < prices.size(); i++) {
                            BigDecimal priceBD = new BigDecimal(Float.parseFloat(prices.get(i).getText()
                                    .substring(0, prices.get(i).getText().length() - 2)
                                    .replace(" ", "")
                                    .replace(",", ".")));

                            addItem(names.get(i).getText(), "Morele", new Date(), priceBD.setScale(2, RoundingMode.HALF_UP),
                                    names.get(i).getAttribute("href"));
                        }
                        WebElement nextPage = driver.findElement(By.xpath("//li[@class='pagination-lg next']\n" +
                                "//a[@class='pagination-btn']"));
                        nextPage.click();
                        isLocationChange = false;
                        errorCounter =System.currentTimeMillis();
                    } catch (Exception e) {
                        loop = false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("skończyły się strony Euro");
            driver.close();
        }
        driver.close();
    }

    public void scrapeEuro(final String value) throws InterruptedException {

        ChromeDriver driver = seleniumConfiguration.driver();


        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        URL = "https://www.euro.com.pl/";
        driver.get(URL);
        try {
            final WebElement placeToWrite = driver.findElement(By.xpath("//input[@id='keyword']"));
            placeToWrite.sendKeys(value);
            final WebElement search = driver.findElement(By.xpath("//a[@class='selenium-search-button']"));
            search.click();
            driver.get(driver.getCurrentUrl());
            List<WebElement> prices;
            List<WebElement> names;
            boolean loop = true;
            String location = "";
            boolean isLocationChange = true;
            int pageCounter = 1;
            long errorCounter =System.currentTimeMillis();
            // pętla, która przeszukuje strony z wynikami
            while (loop) {
                if (pageCounter > 10) {
                    break;
                }
                if (location.equals(driver.getCurrentUrl())) {
                    if (errorCounter + (15000) < System.currentTimeMillis()) {
                        break;
                    }
                    errorCounter++;
                    isLocationChange = false;
                } else {
                    isLocationChange = true;
                    errorCounter = 0;
                    location = driver.getCurrentUrl();
                }
                if (isLocationChange) {
                    try {
                        pageCounter++;
                        driver.get(driver.getCurrentUrl());
                        //waitForLoad(driver);
                        WebElement products = driver.findElement(By.xpath("//div[@id='products']"));
                        prices = (products.findElements(By.xpath("//div[@class='price-normal selenium-price-normal']")));
                        names = (products.findElements(By.xpath("//a[@class='js-save-keyword']")));
                        //pętla dodająca dane do bazy dla konkretnej strony
                        for (int i = 1; i < names.size(); i += 2) {
                            BigDecimal priceBD = new BigDecimal(Float.parseFloat(prices.get(i).getText()
                                    .substring(0, prices.get(i).getText().length() - 2)
                                    .replace(" ", "")
                                    .replace(",", ".")));

                            addItem(names.get(i).getText(), "Euro", new Date(), priceBD.setScale(2, RoundingMode.HALF_UP),
                                    names.get(i).getAttribute("href"));
                        }
                        WebElement nextPage = driver.findElement(By.xpath("//a[@class = 'paging-next selenium-WC-paging-next-button']"));
                        nextPage.click();
                        errorCounter =System.currentTimeMillis();
                    } catch (Exception e) {
                        System.out.println("Skończyły się strony Euro");
                        loop = false;
                        driver.close();
                    }
                }
            }

        } catch (Exception e) {
            driver.close();
        }
        driver.close();
    }
}
