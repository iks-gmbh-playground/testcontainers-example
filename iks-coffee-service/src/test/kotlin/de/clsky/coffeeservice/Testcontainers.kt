package de.clsky.coffeeservice

import org.springframework.context.annotation.*
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import java.time.Duration


object Redis : GenericContainer<Redis>("redis:6.2.6")
object OrderService : GenericContainer<OrderService>("iks-order-service:1.0-SNAPSHOT")
object Postgres: PostgreSQLContainer<Postgres>("postgres:14")

fun withRedisRunning(block: (Redis) -> Unit) {
    block(Redis.apply {
        if (!isRunning) {
            withExposedPorts(6379)
            start()
        }
    })
}

fun withOrderServiceRunning(block: (OrderService) -> Unit) {
    val orderServiceNetwork = Network.newNetwork()
    block(OrderService.apply {
        withPostgresRunning(orderServiceNetwork) {
            withEnv(mapOf(
                "DATABASE_JDBC_URL" to "jdbc:postgresql://postgres:5432/orderservice?loggerLevel=OFF",
                "DATABASE_USERNAME" to it.username,
                "DATABASE_PASSWORD" to it.password
            ))
            withNetwork(orderServiceNetwork)
            withNetworkAliases("order-service")
            withExposedPorts(8080)
            withStartupTimeout(Duration.ofSeconds(30))
            waitingFor(LogMessageWaitStrategy().withRegEx(".*Started OrderServiceApplication.*").withTimes(1))
            start()
        }
    })
}

fun withPostgresRunning(network: Network, block: (Postgres) -> Unit) {
    block(Postgres.apply {
        if (!isRunning) {
            withNetwork(network)
            withNetworkAliases("postgres")
            withUsername("user")
            withPassword("password")
            withDatabaseName("orderservice")
            start()
        }
    })
}

@Profile("ittest")
@Configuration
@Lazy
class TestConfiguration : WebMvcConfigurer {

    @Bean
    @Primary
    fun redisTestConnectionFactory(): RedisConnectionFactory {
        val config = RedisStandaloneConfiguration()
        withRedisRunning {
            config.port = it.firstMappedPort
            config.hostName = it.host
        }
        val lettuceClientConfig = LettuceClientConfiguration.builder().commandTimeout(Duration.ofMillis(10000)).build()
        return LettuceConnectionFactory(config, lettuceClientConfig)
    }
}
