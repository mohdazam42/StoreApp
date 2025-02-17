package com.feature.productlist.presentation.screen

import com.feature.productlist.domain.model.Product

sealed class ProductListState {
    data object Loading: ProductListState()
    data class Success(val productList: List<Product>): ProductListState()
    data class Error(val message: String): ProductListState()
}