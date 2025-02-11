package com.feature.productlist.presentation.screen

import com.feature.productlist.domain.model.Product

sealed interface ProductListContract

data class ProductListState(
    val isLoading: Boolean,
    val productList: List<Product>,
    val error: String
) : ProductListContract

sealed class ProductListEvent : ProductListContract {
    data object LoadProducts : ProductListEvent()
}

sealed interface ProductListSideEffect : ProductListContract {
    data class NavigateToProductDetails(val id: Int) : ProductListSideEffect
}