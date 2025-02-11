package com.feature.productdetail.domain.model


data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val image: String,
    val rating: Rating,
    val description: String,
    val category: String
)

data class Rating(
    val rate: Double,
    val count: Int
)
