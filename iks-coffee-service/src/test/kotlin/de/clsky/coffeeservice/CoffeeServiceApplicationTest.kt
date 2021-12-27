package de.clsky.coffeeservice

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.ClassRule
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("ittest")
class CoffeeServiceApplicationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var esTemplate : ElasticsearchOperations

    @Autowired
    lateinit var coffeeESRepository: CoffeeESRepository

    @Test
    fun grabMyCoffeeTest() {
        var coffee = Coffee("x1", "Bester Kaffee", 0.5)
        coffeeESRepository.save(coffee)
        val userId = "x1"
        mockMvc.get("/users/${userId}/favorite-coffee").andExpect {
            status {
                is2xxSuccessful()
            }
            content {
                json(jacksonObjectMapper().writeValueAsString(coffee))
            }
        }

    }
}