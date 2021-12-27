package de.clsky.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("ittest")
@Testcontainers
@AutoConfigureMockMvc
public class IntegrationsTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected PlatformTransactionManager platformTransactionManager;



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


