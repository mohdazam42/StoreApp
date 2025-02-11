package com.feature.productlist.domain.model


data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val image: String,
    val rating: Rating
)

data class Rating(
    val rate: Double,
    val count: Int
)
