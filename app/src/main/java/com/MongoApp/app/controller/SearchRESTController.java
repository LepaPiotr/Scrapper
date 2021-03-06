package com.MongoApp.app.controller;

import com.MongoApp.app.AppApplication;
import com.MongoApp.app.entity.Search;
import com.MongoApp.app.mongoRepos.SearchRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class SearchRESTController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    SearchRepository searchrepository;

    @PostMapping("/find/{phrase}")
    public void addToSearchQueue(@PathVariable("phrase") String phrase){
        rabbitTemplate.convertAndSend(AppApplication.topicExchangeName, "foo.find", phrase);
        Search search = new Search(phrase, new Date());
        searchrepository.save(search);
    }

}
