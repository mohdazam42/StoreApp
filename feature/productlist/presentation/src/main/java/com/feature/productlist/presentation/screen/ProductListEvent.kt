package com.feature.productlist.presentation.screen

import com.example.common.extensions.SingleValueCallback


sealed class ProductListEvent {
    data object LoadProducts : ProductListEvent()
    data class OnProductClick(val id: Int, val navigateToDetails: SingleValueCallback<Int>) : ProductListEvent()
}