package de.clsky.coffeeservice

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.testcontainers.containers.GenericContainer
import java.time.Duration

object Redis : GenericContainer<Redis>("redis:6.2.6")

fun withRedisRunning(block: (Redis) -> Unit) {
    block(Redis.apply {
        if (!isRunning) {
            withExposedPorts(6379)
            start()
        }
    })
}

@Profile("ittest")
@Configuration
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
