package de.iks.coffeeservice.redis

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.io.Serializable

@RedisHash("Coffee")
data class Coffee(
    @Id val userId: String, val name: String, val amount: Double
) : Serializable

@Repository
interface CoffeRedisRepository : CrudRepository<Coffee, String>