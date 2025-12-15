package com.mongo.app.scrapper.utils;

import com.mongo.app.configuration.SeleniumConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScrapperUtils {
    
    private final SeleniumConfiguration seleniumConfiguration;


    public ChromeDriver createDriver() {
        return seleniumConfiguration.driver();
    }


    public WebElement findElement(String path, ChromeDriver chromeDriver) {
        return chromeDriver.findElement(By.xpath(path));
    }

    public List<WebElement> findElements(String path, ChromeDriver chromeDriver) {
        return chromeDriver.findElements(By.xpath(path));
    }

    public void moveToElement(WebElement webElement, ChromeDriver chromeDriver) {
        Actions actions = new Actions(chromeDriver);
        actions.moveToElement(webElement).perform();
    }

    public void searchPhrase(String searchBarPath, String searchButtonPath, String value, ChromeDriver chromeDriver) {
        final WebElement serachBar = findElement(searchBarPath, chromeDriver);
        serachBar.sendKeys(value);
        final WebElement searchButton = findElement(searchButtonPath, chromeDriver);
        searchButton.click();
    }

    public void getToPage(String value, ChromeDriver chromeDriver) {
        chromeDriver.get(value);
    }

    public BigDecimal getPrice(String price) {
        return BigDecimal.valueOf(Float.parseFloat(price
                .substring(0, price.length() - 2)
                .replace(" ", "")
                .replace(",", ".")));
    }

    public void acceptCokies(String target, ChromeDriver chromeDriver) {
        try {
            final WebElement cookie = findElement(target, chromeDriver);
            cookie.click();
        } finally {
            log.info("Brak przycisku");
        }
    }
}
