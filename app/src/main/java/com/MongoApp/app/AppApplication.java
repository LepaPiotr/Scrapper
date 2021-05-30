package com.MongoApp.app;

import com.MongoApp.app.entity.Search;
import com.MongoApp.app.mongoRepos.SearchRepository;
import com.MongoApp.app.service.ScrapperService;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

@SpringBootApplication
@EnableScheduling
public class AppApplication {

    public static final String topicExchangeName = "spring-boot-exchange";

    static final String queueName = "spring-boot";

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    SearchRepository searchrepository;

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("foo.find");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(ScrapperService receiver) {
        return new MessageListenerAdapter(receiver, "scrapeAll");
    }

    @Scheduled(cron = "0 0 0 * * MON-SUN")
    public void generateSearch() {
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH - 3));
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
                System.out.println("wysyłam zapytanie o wyszukanie hasła " + phrase);
                rabbitTemplate.convertAndSend(AppApplication.topicExchangeName, "foo.find", phrase);
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

}
