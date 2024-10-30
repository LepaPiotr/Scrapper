package com.mongo.app.service;

import com.mongo.app.AppApplication;
import com.mongo.app.entity.Search;
import com.mongo.app.repository.SearchRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@Service
@AllArgsConstructor
public class SearchService {
    RabbitTemplate rabbitTemplate;
    SearchRepository searchrepository;

    public void addToSearchQueue(@PathVariable("phrase") String phrase) {
        rabbitTemplate.convertAndSend(AppApplication.topicExchangeName, "foo.find", phrase);
        Search search = new Search(phrase, new Date());
        searchrepository.save(search);
    }

}
