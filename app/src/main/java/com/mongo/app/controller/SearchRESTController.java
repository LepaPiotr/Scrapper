package com.mongo.app.controller;

import com.mongo.app.service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class SearchRESTController {

    SearchService searchService;

    @PostMapping("/find/{phrase}")
    public void findPhrase(@PathVariable("phrase") String phrase){
        searchService.addToSearchQueue(phrase);
    }

}
