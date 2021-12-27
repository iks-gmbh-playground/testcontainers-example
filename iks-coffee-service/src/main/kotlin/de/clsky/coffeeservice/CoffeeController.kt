package de.clsky.coffeeservice

import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CoffeeController(val coffeeESRepository: CoffeeRepository) {

    @GetMapping("/users/{userId}/favorite-coffee")
    fun getFavoriteCoffee(@PathVariable("userId") userId: String): ResponseEntity<Coffee> {
        return when(val coffee = coffeeESRepository.findCoffeeByUserId(userId, Pageable.unpaged()).firstOrNull()) {
            null -> ResponseEntity.notFound().build()
            else -> ResponseEntity.ok(coffee)
        }
    }
}
