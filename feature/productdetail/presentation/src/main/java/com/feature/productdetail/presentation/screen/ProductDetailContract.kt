package com.feature.productdetail.presentation.screen

import com.feature.productdetail.domain.model.Product

sealed interface ProductDetailContract

data class ProductDetailState(
    val isLoading: Boolean,
    val product: Product,
    val error: String
) : ProductDetailContract

sealed class ProductDetailEvent : ProductDetailContract {
    data class LoadProduct(val productId: Int) : ProductDetailEvent()
    data class OnNavigateBack(val navigateBack: () -> Unit) : ProductDetailEvent()
}
