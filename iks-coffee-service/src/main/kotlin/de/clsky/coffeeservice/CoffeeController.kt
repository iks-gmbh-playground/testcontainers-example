package de.clsky.coffeeservice

import com.fasterxml.jackson.databind.ObjectMapper
import de.clsky.coffeeservice.redis.CoffeRedisRepository
import de.clsky.coffeeservice.redis.Coffee
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CoffeeController(
    val coffeeRedisRepository: CoffeRedisRepository,
) {

    @GetMapping("/users/{userId}/favorite-coffee")
    fun getFavoriteCoffee(@PathVariable("userId") userId: String): ResponseEntity<Coffee> {
        return coffeeRedisRepository.findById(userId).let {
            when {
                it.isEmpty -> ResponseEntity.notFound().build()
                else -> ResponseEntity.ok(it.get())
            }
        }
    }
}
