package de.clsky.coffeeservice

import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface CoffeeRepository : ElasticsearchRepository<Coffee, String> {
    fun findCoffeeByUserId(userId: String, pageable: Pageable) : List<Coffee>
}