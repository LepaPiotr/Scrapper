package com.mongo.app.service;

import com.mongo.app.scrapper.Scrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
@Service
@AllArgsConstructor
@Slf4j
public class ScrapperService {

    private final ExecutorService executor;
    private final List<Scrapper> scrappers;

    public void scrapeAll(String message) {
        try {
            for (Scrapper scrapper : scrappers) {
                executor.submit(() -> scrapper.scrape(message));
            }
        } catch (Exception e) {
            log.error("Błąd podczas wykonywania scrapeAll: {}", e.getMessage(), e);
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            log.error("Wątki zostały przerwane: {}", e.getMessage(), e);
        }
    }
}

