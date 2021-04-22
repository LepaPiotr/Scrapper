package com.MongoApp.app.controller;

import com.MongoApp.app.AppApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FindRESTController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostMapping("/find/{phrase}")
    public void addToSearchQueue(@PathVariable("phrase") String phrase){
        rabbitTemplate.convertAndSend(AppApplication.topicExchangeName, "foo.find", phrase);

    }

}
