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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class MoreleScrapper {

    private final ScrapperUtils scrapperUtils;

    public int scrape() {

        int driversCount = Runtime.getRuntime().availableProcessors() > 5
                ? Runtime.getRuntime().availableProcessors() * 2
                : Runtime.getRuntime().availableProcessors();

        CountDownLatch pageLoadedBarrier = new CountDownLatch(driversCount);
        AtomicInteger successCount = new AtomicInteger(0);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            IntStream.range(0, driversCount)
                    .forEach(i -> executor.submit(() -> {

                        ChromeDriver driver = null;
                        boolean pageLoaded = false;

                        try {
                            try {
                                driver = scrapperUtils.createDriver();
                                scrapperUtils.getToPage("https://nakarmpsa.olx.pl", driver);
                                pageLoaded = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                pageLoadedBarrier.countDown();
                            }
                            pageLoadedBarrier.await(20, TimeUnit.SECONDS);

                            if (!pageLoaded || driver == null) {
                                return;
                            }

                            scrapperUtils.acceptCokies(
                                    "//*[@id=\"onetrust-accept-btn-handler\"]",
                                    driver
                            );

                            List<WebElement> elements =
                                    scrapperUtils.findElements(
                                            "//*[@class=\"single-pet\"]/div/div[4]/div[2]/div[1]",
                                            driver
                                    );

                            WebElement dog = elements.get(new Random().nextInt(elements.size()));
                            scrapperUtils.moveToElement(dog, driver);
                            dog.click();

                            Thread.sleep(500);
                            successCount.incrementAndGet();

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (driver != null) {
                                try {
                                    driver.quit();
                                } catch (Exception ignored) {}
                            }
                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return successCount.get();
    }


}
