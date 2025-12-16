package com.mongo.app.scrapper;

import com.mongo.app.scrapper.utils.ScrapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class MoreleScrapper implements Scrapper {
    @Override
    public void scrape(String value) {
        ChromeDriver driver = ScrapperUtils.createDriver();
        ScrapperUtils.getToPage("https://www.morele.net/", driver);

        ScrapperUtils.acceptCokies("//button[@class='btn btn-secondary btn-secondary-outline btn-md close-cookie-box']", driver);

        String searchBar = "//input[@name='search']']";
        String search = "//button[@class='btn btn-primary h-quick-search-submit']";
        ScrapperUtils.searchPhrase(searchBar, search, value, driver);

        int pageCounter = 1;
        while (pageCounter <= 10) {
            try {
                pageCounter++;
                WebElement products = ScrapperUtils.findElement("//div[@class='category-list']", driver);
                List<WebElement> prices = products.findElements(By.xpath("//div[@class='price-new']"));
                List<WebElement> names = products.findElements(By.xpath("//a[@class='productLink']"));

                for (int i = 0; i < prices.size(); i++) {
                    BigDecimal priceBD = ScrapperUtils.getPrice(prices.get(i).getText());

                    ScrapperUtils.addItem(names.get(i).getText(), "Morele", new Date(), priceBD.setScale(2, RoundingMode.HALF_UP),
                            names.get(i).getAttribute("href"));
                }
                ScrapperUtils.getToPage("//li[@class='pagination-lg next']\n" +
                        "//a[@class='pagination-btn']", driver);
            } catch (Exception e) {
                break;
            }
        }
        driver.quit();
    }
}
