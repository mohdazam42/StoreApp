package com.feature.productlist.presentation.screen

import com.example.common.extensions.SingleValueCallback
import com.feature.productlist.domain.model.Product

data class ProductListState(
    val isLoading: Boolean = false,
    val productList: List<Product> = emptyList(),
    val error: String = ""
)

sealed class ProductListEvent {
    data object LoadProducts : ProductListEvent()
    data class OnProductClick(val id: Int, val navigateToDetails: SingleValueCallback<Int>) : ProductListEvent()
}