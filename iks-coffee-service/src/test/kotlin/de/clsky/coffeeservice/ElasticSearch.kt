package de.clsky.coffeeservice

import org.elasticsearch.client.RestHighLevelClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.RestClients
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName

object ElasticSearch :
    ElasticsearchContainer(DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.16.2"))

fun withESRunning(block: (ElasticSearch) -> Unit) {
    block(ElasticSearch.apply {
        if (!isRunning) start()
    })
}

@Profile("ittest")
@Configuration
@Lazy
class ElasticSearchTestConfiguration : WebMvcConfigurer {

    @Bean
    @Primary
    fun client(): RestHighLevelClient = ClientConfiguration.builder().let {
        var url: String? = null
        withESRunning { es ->
            url = es.httpHostAddress
        }
        it.connectedTo(url)
            .build()
            .let { config ->
                RestClients.create(config).rest()
            }

    }
}
