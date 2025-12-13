package com.mongo.app.scrapper;

import com.mongo.app.scrapper.utils.ScrapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
@AllArgsConstructor
@Slf4j
public class MoreleScrapper implements Scrapper {
    private final ScrapperUtils scrapperUtils;

    @Override
    public void scrape(String value) {
        ChromeDriver driver = scrapperUtils.createDriver();
        scrapperUtils.getToPage("https://nakarmpsa.olx.pl", driver);

        scrapperUtils.acceptCokies("//*[@id=\"onetrust-accept-btn-handler\"]", driver);

        List<WebElement> elements = scrapperUtils.findElements("//*[@class=\"single-pet\"]/div/div[4]/div[2]/div[1]", driver);
        WebElement dog = elements.get(new Random().nextInt(17));
        scrapperUtils.moveToElement(dog, driver);
        dog.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.close();

    }
}
