package com.feature.productdetail.presentation.screen


sealed class ProductDetailEvent {
    data class LoadProduct(val productId: Int) : ProductDetailEvent()
    data class OnNavigateBack(val navigateBack: () -> Unit) : ProductDetailEvent()
}
