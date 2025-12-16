package com.mongo.app.scrapper;

import com.mongo.app.scrapper.utils.ScrapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class OlxScrapper {

    private final ScrapperUtils scrapperUtils;

    public void feedDogs() {

        int driversCount = Runtime.getRuntime().availableProcessors() > 5
                ? Runtime.getRuntime().availableProcessors() * 2
                : Runtime.getRuntime().availableProcessors();

        AtomicInteger successCount = new AtomicInteger(0);

        Random random = new Random();

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        IntStream.range(0, driversCount)
                .forEach(i -> executor.submit(() -> {

                    while (!Thread.currentThread().isInterrupted()) {
                        ChromeDriver driver = null;
                        try {
                            driver = scrapperUtils.createDriver();
                            scrapperUtils.getToPage("https://nakarmpsa.olx.pl", driver);
                            scrapperUtils.findButton("//*[@id=\"onetrust-accept-btn-handler\"]", driver);
                            changePage(driver, random);
                            List<WebElement> elements = scrapperUtils.findElements("//*[@class=\"single-pet\"]/div/div[4]/div[2]/div[1]", driver);
                            WebElement dog = elements.get(random.nextInt(elements.size()));
                            scrapperUtils.moveToElement(dog, driver);
                            dog.click();
                            log.info("clicks: " + successCount.incrementAndGet());

                        } catch (Exception e) {
                           log.error("Error in the thread: " + e.getMessage());
                        } finally {
                            if (driver != null) {
                                try {
                                    driver.quit();
                                } catch (Exception e) {
                                    log.error("Error on quiting driver: " + e.getMessage());
                                }
                            }
                        }
                    }
                }));
    }

    private void changePage (ChromeDriver driver, Random random){
        int rand = random.nextInt(4);
        if(rand == 3) return;

        List<WebElement> pages = new ArrayList<>();
        pages.add(scrapperUtils.findElement(
                "//*[@id=\"block_d8de5bfc062d248349f40f274e06ef68\"]/div/div/div[3]/div/div[2]", driver));
        pages.add(scrapperUtils.findElement(
                "//*[@id=\"block_d8de5bfc062d248349f40f274e06ef68\"]/div/div/div[3]/div/div[3]", driver));
        pages.add(scrapperUtils.findElement(
                "//*[@id=\"block_d8de5bfc062d248349f40f274e06ef68\"]/div/div/div[3]/div/div[4]", driver));

        pages.get(rand).click();
    }


}
