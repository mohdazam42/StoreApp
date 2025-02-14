package com.feature.productlist.presentation.screen

import com.feature.productlist.domain.model.Product

data class ProductListState(
    val isLoading: Boolean = false,
    val productList: List<Product> = emptyList(),
    val error: String = ""
)