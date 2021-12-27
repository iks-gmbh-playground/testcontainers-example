package de.clsky.coffeeservice

import org.elasticsearch.client.RestHighLevelClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.RestClients
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@Configuration
@EnableElasticsearchRepositories
class ElasticSearchConfiguration(val props: ElasticSearchProperties) : AbstractElasticsearchConfiguration() {

    @Bean
    fun elasticsearchTemplate() = ElasticsearchRestTemplate(elasticsearchClient())

    @Bean
    override fun elasticsearchClient(): RestHighLevelClient = ClientConfiguration.builder()
        .connectedTo(props.url)
        .build()
        .let {
            RestClients.create(it).rest()
        }

}

@ConfigurationProperties(prefix = "clients.elasticsearch")
class ElasticSearchProperties {
    lateinit var url: String
}
