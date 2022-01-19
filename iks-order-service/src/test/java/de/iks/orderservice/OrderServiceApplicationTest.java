package de.iks.orderservice;


import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderServiceApplicationTest extends IntegrationsTest {

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
            assertThat(orders.size()).isEqualTo(0);
            return null;
        });
    }
}
