package de.clsky.coffeeservice

data class Order(
    private var id: Long? = null,
    val amount: Long? = null,
    val itemName: String? = null
)