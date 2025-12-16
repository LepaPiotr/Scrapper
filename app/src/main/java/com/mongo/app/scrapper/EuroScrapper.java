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
public class EuroScrapper implements Scrapper {
    @Override
    public void scrape(String value) {
        ChromeDriver driver = ScrapperUtils.createDriver();
        ScrapperUtils.getToPage("https://www.euro.com.pl/", driver);
        String searchBar = "//input[@id='keyword']";
        String search = "//a[@class='selenium-search-button']";
        ScrapperUtils.searchPhrase(searchBar, search, value, driver);

        int pageCounter = 1;
        // pętla, która przeszukuje strony z wynikami
        while (pageCounter <= 10) {
            try {
                pageCounter++;
                WebElement products = ScrapperUtils.findElement("//div[@id='products']", driver);
                List<WebElement> prices = (products.findElements(By.xpath("//div[@class='price-normal selenium-price-normal']")));
                List<WebElement> names = (products.findElements(By.xpath("//a[@class='js-save-keyword']")));
                //pętla dodająca dane do bazy dla konkretnej strony
                for (int i = 1; i < names.size(); i += 2) {
                    BigDecimal priceBD = ScrapperUtils.getPrice(prices.get(i).getText());

                    ScrapperUtils.addItem(names.get(i).getText(), "Euro", new Date(), priceBD.setScale(2, RoundingMode.HALF_UP),
                            names.get(i).getAttribute("href"));
                }
                ScrapperUtils.getToPage("//a[@class = 'paging-next selenium-WC-paging-next-button']", driver);
            } catch (Exception e) {
                log.info("Skończyły się strony Euro");
                break;
            }
        }
        driver.quit();

    }
}
