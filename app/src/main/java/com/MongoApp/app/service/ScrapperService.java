package com.MongoApp.app.service;

import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ScrapperService {

    private static String URL ;

    private ChromeDriver driver;

    public void scrapeXKom(final String value) throws InterruptedException {
        URL = "https://www.x-kom.pl";
        driver.get(URL);
        System.out.println(URL + value);
        final WebElement placeToWrite = driver.findElement(By.xpath("//input[@class='sc-1hdf4hr-0 frAjNp']"));
        placeToWrite.sendKeys(value);
        final WebElement search = driver.findElement(By.xpath("//button[@class='sc-15gi9e9-7 apKoa']"));
        search.click();
        driver.get(driver.getCurrentUrl());
        final WebElement products = driver.findElement(By.xpath("//div[@id='listing-container']"));
        final List<WebElement> prices = products.findElements(By.xpath("//span[@class='sc-6n68ef-0 sc-6n68ef-3 hNZEsQ']"));
        final List<WebElement> names = products.findElements(By.xpath("//a[@class='sc-1h16fat-0 irSQpN']"));
        System.out.println(names.size());
        System.out.println(prices.size());
        for (int i = 0; i < prices.size(); i++) {
            System.out.println(i + ".  " + names.get(i).getText() + " " + prices.get(i).getText());
        }
    }

    public void scrapeMorele(final String value) throws InterruptedException {
        URL = "https://www.morele.net/";
        driver.get(URL);
        System.out.println(URL + value);
        final WebElement placeToWrite = driver.findElement(By.xpath("//input[@name='search']"));
        placeToWrite.sendKeys(value);
        final WebElement search = driver.findElement(By.xpath("//button[@class='btn btn-primary h-quick-search-submit']"));
        search.click();
        driver.get(driver.getCurrentUrl());
        final WebElement products = driver.findElement(By.xpath("//div[@class='category-list']"));
        final List<WebElement> prices = products.findElements(By.xpath("//div[@class='price-new']"));
        final List<WebElement> names = products.findElements(By.xpath("//a[@class='productLink']"));
        System.out.println(names.size());
        System.out.println(prices.size());
        for (int i = 0; i < prices.size(); i++) {
            System.out.println(i + ".  " + names.get(i).getText() + " " + prices.get(i).getText());
        }
    }

    public void scrapeMediaExpert(final String value) throws InterruptedException {
        URL = "https://www.mediaexpert.pl";
        driver.get(URL);
        System.out.println(URL + value);
        final WebElement placeToWrite = driver.findElement(By.xpath("//input[@name='search']"));
        placeToWrite.sendKeys(value);
        final WebElement search = driver.findElement(By.xpath("//div[@class='c-search_submit']"));
        search.click();
        driver.get(driver.getCurrentUrl());
        final WebElement products = driver.findElement(By.xpath("//div[@class='c-grid is-equal is-offers is-lazyLoadContainer is-loaded']"));
        List<WebElement> prices = products.findElements(By.xpath("//div[@class='c-offerBox_price is-normalPrice is-promoPrice']\n" +
                "//span[@class='a-price_price']"));
        prices.addAll(products.findElements(By.xpath("//div[@class='c-offerBox_price is-normalPrice ']\n" +
                "                //span[@class='a-price_price']")));
        final List<WebElement> names = products.findElements(By.xpath("//h2[@class='c-offerBox_data ']\n" +
                "//a[@class='a-typo is-secondary']"));
        System.out.println(names.size());
        System.out.println(prices.size());
        for (int i = 0; i < prices.size(); i++) {
            System.out.println(i + ".  " + names.get(i).getText() + " " + prices.get(i).getText());
        }
    }
    public void scrapeMediaMarkt(final String value) throws InterruptedException {
        URL = "https://mediamarkt.pl/";
        driver.get(URL);
        System.out.println(URL + value);
        final WebElement placeToWrite = driver.findElement(By.xpath("//input[@id='query_querystring']"));
        placeToWrite.sendKeys(value);
        final WebElement search = driver.findElement(By.xpath("//button[@id='js-triggerSearch']"));
        search.click();
        driver.get(driver.getCurrentUrl());
        final WebElement products = driver.findElement(By.xpath("//div[@class='b-row clearfix2 b-listing_classic js-eqContainer js-offerBox js-equalHRow']"));
        final List<WebElement> prices = products.findElements(By.xpath("//div[@itemprop='price']"));
        final List<WebElement> names = products.findElements(By.xpath("//a[@class='b-ofr_headDataTitle']"));
        System.out.println(names.size());
        System.out.println(prices.size());
        for (int i = 0; i < prices.size(); i++) {
            System.out.println(i + ".  " + names.get(i).getText() + " " + prices.get(i).getText());
        }
    }

    public void scrapeEuro(final String value) throws InterruptedException {
        URL = "https://www.euro.com.pl/";
        driver.get(URL);
        System.out.println(URL + value);
        final WebElement placeToWrite = driver.findElement(By.xpath("//input[@class='selenium-keyword-input pf-atocomplete-desktop']"));
        placeToWrite.sendKeys(value);
        final WebElement search = driver.findElement(By.xpath("//a[@class='selenium-search-button']"));
        search.click();
        driver.get(driver.getCurrentUrl());
        final WebElement products = driver.findElement(By.xpath("//div[@id='products']"));
        final List<WebElement> prices = products.findElements(By.xpath("//div[@class='price-normal selenium-price-normal']"));
        final List<WebElement> names = products.findElements(By.xpath("//a[@class='js-save-keyword']"));
        System.out.println(names.size());
        System.out.println(prices.size());
        for (int i = 1; i < prices.size(); i+=2) {
            System.out.println(i + ".  " + names.get(i).getText() + " " + prices.get(i).getText());
        }
    }


}
