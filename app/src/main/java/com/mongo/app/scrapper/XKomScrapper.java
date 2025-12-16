package com.mongo.app.scrapper;

import com.mongo.app.scrapper.utils.ScrapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j

public class XKomScrapper implements Scrapper {

    @Override
    public void scrape(String value) {
        ChromeDriver driver = ScrapperUtils.createDriver();
        ScrapperUtils.getToPage("https://www.x-kom.pl", driver);
        String searchBar = "//input[@class='sc-1hdf4hr-0 frAjNp']";
        String search = "//button[@class='sc-15gi9e9-7 apKoa']";
        ScrapperUtils.searchPhrase(searchBar, search, value, driver);
        int counter = 1;
        while (counter <= 10) {
            try {
                counter++;
                WebElement products = ScrapperUtils.findElement("//div[@id='listing-container']", driver);
                List<WebElement> prices = (products.findElements(By.xpath("//span[@class='sc-6n68ef-0 sc-6n68ef-3 hNZEsQ']")));
                List<WebElement> names = (products.findElements(By.xpath("//a[@class='sc-1h16fat-0 irSQpN']")));
                for (int i = 0; i < prices.size(); i++) {
                    BigDecimal priceBD = ScrapperUtils.getPrice(prices.get(i).getText());
                    ScrapperUtils.addItem(names.get(i).getText(), "X-kom", new Date(), priceBD.setScale(2, RoundingMode.HALF_UP),
                            names.get(i).getAttribute("href"));
                }
                WebElement nextPage = ScrapperUtils.findElement("//div[@class='sc-11oikyw-0 jeEhfJ']\n" +
                        "//a[@class='sc-11oikyw-3 fcPVMJ sc-1h16fat-0 irSQpN']", driver);
                nextPage.click();
            } catch (Exception e) {
                log.info("end of pages");
                break;
            }
        }
        driver.quit();
    }
}
