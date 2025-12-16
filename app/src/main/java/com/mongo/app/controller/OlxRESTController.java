package com.mongo.app.controller;

import com.mongo.app.scrapper.OlxScrapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/olx")
@CrossOrigin
@AllArgsConstructor
public class OlxRESTController {

    OlxScrapper olxScrapper;

    @GetMapping()
    public void feedDogs() {
        olxScrapper.feedDogs();
    }

}
