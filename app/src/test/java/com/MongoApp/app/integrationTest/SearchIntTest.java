package com.MongoApp.app.integrationTest;


import com.mongo.app.repository.SearchRepository;
import com.mongo.app.entity.Search;
import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@AllArgsConstructor
public class SearchIntTest {
    private MockMvc mvc;
    private SearchRepository searchRepository;

    @Test
    public void findByDateRestTest() throws Exception {
        Date date = new Date();
        searchRepository.save(new Search("Test" , new Date()));
        mvc.perform(MockMvcRequestBuilders.get("/find/Test"));
        List<Search> searchList = searchRepository.findBySearchDateGreaterThanEqual(date);
        if(searchList != null && !searchList.isEmpty() & searchList.get(0).getPhrase().equals("Test"))
            assert true;
        else
            assert false;
    }
}
