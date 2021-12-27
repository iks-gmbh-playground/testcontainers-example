package de.clsky.coffeeservice

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "coffee")
data class Coffee(@Id val userId: String, val name: String, val amount: Double)