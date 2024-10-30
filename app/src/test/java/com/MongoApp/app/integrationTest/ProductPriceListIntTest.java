package com.MongoApp.app.integrationTest;


import com.mongo.app.entity.ProductPriceList;
import com.mongo.app.repository.ProductPriceListRepository;
import lombok.AllArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
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

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@AllArgsConstructor
public class ProductPriceListIntTest {

    private MockMvc mvc;
    private ProductPriceListRepository productPriceListRepository;

    @Test
    public void findByNameIdRestTest() throws Exception {
        productPriceListRepository.save(new ProductPriceList("Test", "Test", new Date(), BigDecimal.ONE, "Test"));
        mvc.perform(MockMvcRequestBuilders.get("/productPriceList/id/Test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productId", CoreMatchers.is("Test")));
    }
}
