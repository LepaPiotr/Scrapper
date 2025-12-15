package com.mongo.app.controller;

import com.mongo.app.scrapper.MoreleScrapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.List;

@RestController
@RequestMapping("/olx")
@CrossOrigin
@AllArgsConstructor
public class OlxRESTController {

    MoreleScrapper moreleScrapper;

    @GetMapping()
    public void findAll() {
        long counter = 0;
        while (true) {
            moreleScrapper.scrape("any");
            System.out.println(++counter);
        }
    }

}
