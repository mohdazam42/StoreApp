package com.feature.productdetail.presentation.screen

import com.feature.productdetail.domain.model.Product

sealed interface ProductDetailState {
    data object Loading: ProductDetailState
    data class Success(val product: Product): ProductDetailState
    data class Error(val message: String): ProductDetailState
}