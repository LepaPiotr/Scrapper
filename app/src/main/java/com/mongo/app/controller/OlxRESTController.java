package com.mongo.app.controller;

import com.mongo.app.scrapper.MoreleScrapper;
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

    MoreleScrapper moreleScrapper;

    @GetMapping()
    public void feedDogs() {
        moreleScrapper.feedDogs();
    }

}
