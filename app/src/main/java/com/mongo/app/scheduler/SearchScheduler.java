package com.mongo.app.scheduler;

import com.mongo.app.entity.Search;
import com.mongo.app.repository.SearchRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

@AllArgsConstructor
public class SearchScheduler {

    private final RabbitTemplate rabbitTemplate;
    private final SearchRepository searchrepository;


    @Scheduled(cron = "0 0 0 * * MON-SUN")
    public void generateSearch() {
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -3);
        List<Search> searchList = searchrepository.findBySearchDateGreaterThanEqual(cal.getTime());
        HashSet<String> phraseHash = new HashSet<>();
        if (searchList != null && !searchList.isEmpty()) {
            for (Search search : searchList) {
                if (search.getPhrase() != null && !search.getPhrase().isEmpty())
                    phraseHash.add(search.getPhrase().trim());
            }
        }
        if (!phraseHash.isEmpty()) {
            for (String phrase : phraseHash) {
                rabbitTemplate.convertAndSend("spring-boot-exchange", "foo.find", phrase);
            }
        }
    }

}
