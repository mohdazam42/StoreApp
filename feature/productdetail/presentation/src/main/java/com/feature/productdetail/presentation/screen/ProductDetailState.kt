package com.feature.productdetail.presentation.screen

import com.feature.productdetail.domain.model.Product
import com.feature.productdetail.domain.model.Rating

data class ProductDetailState(
    val isLoading: Boolean = false,
    val product: Product = Product(
        id = 0,
        title = "",
        price = 0.0,
        image = "",
        rating = Rating(
            rate = 0.0,
            count = 0
        ),
        description = "",
        category = ""
    ),
    val error: String = ""
)