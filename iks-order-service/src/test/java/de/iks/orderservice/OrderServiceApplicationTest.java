package de.iks.orderservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("ittest")
@Testcontainers
@AutoConfigureMockMvc
class OrderServiceApplicationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected PlatformTransactionManager platformTransactionManager;

    @Test
    void funktioniertSoweit() throws Exception {
        var myOrder = new Order();
        myOrder.setAmount(1L);
        myOrder.setItemName("Cool");
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .content(mapper.writeValueAsString(myOrder))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        transactional(status -> {
            var orders = orderRepository.findAll();
            assertThat(orders.size()).isEqualTo(1);
            assertThat(orders.get(0).getItemName()).isEqualTo("Cool");
            return null;
        });
    }

    @Test
    void derNicht() throws Exception {
        var myOrder = new Order();
        myOrder.setAmount(1L);
        myOrder.setItemName("not_valid");
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .content(mapper.writeValueAsString(myOrder))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        transactional(status -> {
            var orders = orderRepository.findAll();
            assertThat(orders.size()).isZero();
            return null;
        });
    }

    protected void transactional(TransactionCallback<Void> action) {
        var transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.execute(action);
    }

    @BeforeEach
    protected void clearDB() {
        transactional(status -> {
            orderRepository.deleteAll();
            return null;
        });
    }
}
