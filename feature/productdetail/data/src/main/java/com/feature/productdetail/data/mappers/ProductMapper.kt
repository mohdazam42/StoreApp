package com.feature.productdetail.data.mappers

import com.feature.productdetail.data.model.ProductDto
import com.feature.productdetail.data.model.RatingDto
import com.feature.productdetail.domain.model.Product
import com.feature.productdetail.domain.model.Rating

fun ProductDto.toDomain(): Product = with(receiver = this) {
    Product(
        id = id,
        title = title,
        price = price,
        image = image,
        rating = rating.toDomain(),
        description = this.description,
        category = this.category.replaceFirstChar {
            if (it.isLowerCase()) it.uppercase() else it.toString()
        }
    )
}

fun RatingDto.toDomain(): Rating = with(receiver = this) {
    Rating(
        rate = rate,
        count = count
    )
}