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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
@Slf4j
public class MoreleScrapper implements Scrapper {
    private final ScrapperUtils scrapperUtils;

    @Override
    public void scrape(String value) {
        IntStream.range(0, Runtime.getRuntime().availableProcessors())
                .parallel()
                .forEach(i -> {

                    ChromeDriver driver = null;

                    try {
                        driver = scrapperUtils.createDriver();
                        scrapperUtils.getToPage("https://nakarmpsa.olx.pl", driver);

                        scrapperUtils.acceptCokies("//*[@id=\"onetrust-accept-btn-handler\"]", driver);

                        List<WebElement> elements =
                                scrapperUtils.findElements("//*[@class=\"single-pet\"]/div/div[4]/div[2]/div[1]", driver);

                        WebElement dog = elements.get(new Random().nextInt(elements.size()));
                        scrapperUtils.moveToElement(dog, driver);
                        dog.click();

                        Thread.sleep(500);

                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        if (driver != null) {
                            try {
                                driver.quit();
                            } catch (Exception ignored) {}
                        }
                    }
                });
    }
}
