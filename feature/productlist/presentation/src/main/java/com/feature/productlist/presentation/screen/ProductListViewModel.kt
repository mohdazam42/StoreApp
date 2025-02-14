package com.feature.productlist.presentation.screen

import androidx.lifecycle.viewModelScope
import com.example.common.base.ApiResult
import com.example.common.base.BaseViewModel
import com.feature.productlist.domain.usecase.GetProductListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val getProductListUseCase: GetProductListUseCase) :
    BaseViewModel<ProductListEvent, ProductListState>() {

    private fun fetchAllProducts() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            when (val result = getProductListUseCase.invoke()) {
                is ApiResult.Error -> updateState {
                    it.copy(isLoading = false, error = result.message)
                }
                is ApiResult.Success -> updateState {
                    it.copy(isLoading = false, productList = result.data)
                }
            }
        }
    }

    override fun initState(): ProductListState = ProductListState()

    override fun onEvent(event: ProductListEvent) {
        when (event) {
            ProductListEvent.LoadProducts -> fetchAllProducts()
            is ProductListEvent.OnProductClick -> {
                event.navigateToDetails(event.id)
            }
        }
    }
}