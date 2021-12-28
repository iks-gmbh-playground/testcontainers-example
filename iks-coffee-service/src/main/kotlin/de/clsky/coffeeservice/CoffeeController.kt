package de.clsky.coffeeservice

import de.clsky.coffeeservice.redis.CoffeRedisRepository
import de.clsky.coffeeservice.redis.Coffee
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.postForEntity

@RestController
class CoffeeController(
    val coffeeRedisRepository: CoffeRedisRepository,
    val orderServiceProperties: OrderServiceProperties,
    val restTemplateBuilder: RestTemplateBuilder
) {

    @GetMapping("/users/{userId}/favorite-coffee")
    fun getFavoriteCoffee(@PathVariable("userId") userId: String): ResponseEntity<Coffee> {
        return coffeeRedisRepository.findById(userId).let {
            when {
                it.isEmpty -> ResponseEntity.notFound().build()
                else -> {
                    val coffee = it.get()
                    val response = restTemplateBuilder.build()
                        .postForEntity<Order>("${orderServiceProperties.url}/orders", Order(amount = coffee.amount.toLong(), itemName = coffee.name))
                    if (response.statusCode == HttpStatus.OK) {
                        ResponseEntity.ok(it.get())
                    } else {
                        println(response.statusCode)
                        ResponseEntity.internalServerError().build()
                    }
                }
            }
        }
    }
}
