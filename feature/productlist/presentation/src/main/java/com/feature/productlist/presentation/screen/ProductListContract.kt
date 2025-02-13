package com.feature.productlist.presentation.screen

import com.example.common.extensions.SingleValueCallback
import com.feature.productlist.domain.model.Product

sealed interface ProductListContract

data class ProductListState(
    val isLoading: Boolean,
    val productList: List<Product>,
    val error: String
) : ProductListContract

sealed class ProductListEvent : ProductListContract {
    data object LoadProducts : ProductListEvent()
    data class OnProductClick(val id: Int, val navigateToDetails: SingleValueCallback<Int>) : ProductListEvent()
}