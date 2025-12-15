package com.mongo.app.scrapper;

import com.mongo.app.scrapper.utils.ScrapperUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class MoreleScrapper {

    private final ScrapperUtils scrapperUtils;

    public void scrape() {

        int driversCount = Runtime.getRuntime().availableProcessors() > 5 ? Runtime.getRuntime().availableProcessors() * 2 : Runtime.getRuntime().availableProcessors();

        Semaphore chromeLimit = new Semaphore(driversCount);
        CountDownLatch pageLoadedBarrier = new CountDownLatch(driversCount);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            IntStream.range(0, driversCount)
                    .forEach(i -> executor.submit(() -> {

                        ChromeDriver driver = null;

                        try {
                            chromeLimit.acquire();
                            driver = scrapperUtils.createDriver();
                            scrapperUtils.getToPage("https://nakarmpsa.olx.pl", driver);
                            pageLoadedBarrier.countDown();
                            pageLoadedBarrier.await();
                            scrapperUtils.acceptCokies(
                                    "//*[@id=\"onetrust-accept-btn-handler\"]",
                                    driver
                            );

                            List<WebElement> elements =
                                    scrapperUtils.findElements(
                                            "//*[@id=\"pet-dyzio\"]/div/div[4]/div[2]/div[1]",
                                            driver
                                    );

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
                            chromeLimit.release();
                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
