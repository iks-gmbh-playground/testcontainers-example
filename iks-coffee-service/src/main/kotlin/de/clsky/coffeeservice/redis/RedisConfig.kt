package de.clsky.coffeeservice.redis

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.data.redis.connection.RedisConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import java.time.Duration


@Configuration
class RedisConfig(val redisProperties: RedisProperties) : RedisConfiguration {
    @Bean
    @Lazy
    fun redisConnectionFactory(): RedisConnectionFactory {
        val config = RedisStandaloneConfiguration().apply {
            hostName = redisProperties.url!!
            port = redisProperties.port!!
        }
        val lettuceClientConfig = LettuceClientConfiguration.builder().commandTimeout(Duration.ofMillis(10000)).build()
        return LettuceConnectionFactory(config, lettuceClientConfig)
    }

    @Bean
    @Lazy
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): StringRedisTemplate {
        val template = StringRedisTemplate()
        template.setConnectionFactory(redisConnectionFactory)
        return template
    }

}


@ConfigurationProperties(prefix = "clients.redis")
class RedisProperties {
    var url: String? = null
    var port: Int? = null
}

