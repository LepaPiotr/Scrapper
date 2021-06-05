package com.MongoApp.app.integrationTest;


import com.MongoApp.app.entity.ProductPriceList;
import com.MongoApp.app.entity.Search;
import com.MongoApp.app.mongoRepos.ProductPriceListRepository;
import com.MongoApp.app.mongoRepos.SearchRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class SearchIntTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
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
