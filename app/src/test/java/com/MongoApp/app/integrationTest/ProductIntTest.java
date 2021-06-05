package com.MongoApp.app.integrationTest;


import com.MongoApp.app.entity.Product;
import com.MongoApp.app.mongoRepos.ProductRepository;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ProductIntTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void findByNameRestTest() throws Exception {
        productRepository.save(new Product("Test", "Test", new Date(), BigDecimal.ONE ,""));
        mvc.perform(MockMvcRequestBuilders.get("/product/name/Test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", CoreMatchers.is("Test")));

    }
}
