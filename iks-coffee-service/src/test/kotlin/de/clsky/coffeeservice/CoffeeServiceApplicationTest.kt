package de.clsky.coffeeservice

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.clsky.coffeeservice.redis.CoffeRedisRepository
import de.clsky.coffeeservice.redis.Coffee
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
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
    lateinit var coffeeRedisRepository: CoffeRedisRepository

    @BeforeEach
    fun resetES() {
        coffeeRedisRepository.deleteAll()
    }

    @Test
    fun `Grab my favorite coffee from redis`() {
        val coffee = Coffee("redis1", "Bester Kaffee", 0.5)
        coffeeRedisRepository.save(coffee)
        val userId = "redis1"

        mockMvc.get("/users/${userId}/favorite-coffee").andExpect {
                status {
                    is2xxSuccessful()
                }
                content {
                    json(jacksonObjectMapper().writeValueAsString(coffee))
                }
            }
    }

    @Test
    fun `No one knows my favorite Coffee`() {
        val coffee = Coffee("redis2", "Noch Besterer Kaffee", 0.5)
        coffeeRedisRepository.save(coffee)
        val userId = "redis1"
        mockMvc.get("/users/${userId}/favorite-coffee").andExpect {
                status {
                    isNotFound()
                }
            }
    }

}