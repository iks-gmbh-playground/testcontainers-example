package de.clsky.coffeeservice

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "clients.orderservice")
class OrderServiceProperties {
    var url: String? = ""
}