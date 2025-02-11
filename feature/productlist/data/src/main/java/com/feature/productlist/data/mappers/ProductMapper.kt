package com.feature.productlist.data.mappers

import com.feature.productlist.data.model.ProductDto
import com.feature.productlist.data.model.RatingDto
import com.feature.productlist.domain.model.Product
import com.feature.productlist.domain.model.Rating

fun ProductDto.toDomain(): Product = with(receiver = this) {
    Product(
        id = id,
        title = title,
        price = price,
        image = image,
        rating = rating.toDomain()
    )
}

fun RatingDto.toDomain(): Rating = with(receiver = this) {
    Rating(
        rate = rate,
        count = count
    )
}