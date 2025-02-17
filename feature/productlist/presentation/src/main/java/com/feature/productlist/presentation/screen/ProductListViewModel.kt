package com.feature.productlist.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.base.ApiResult
import com.feature.productlist.domain.usecase.GetProductListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val getProductListUseCase: GetProductListUseCase) :
    ViewModel() {

    private val _state: MutableStateFlow<ProductListState> = MutableStateFlow(ProductListState.Loading)
    val state = _state.asStateFlow()
    private var isLoaded = false

    private fun fetchAllProducts() {
        if (isLoaded) return
        viewModelScope.launch {
            _state.value = ProductListState.Loading
            when (val result = getProductListUseCase()) {
                is ApiResult.Error ->  {
                    _state.value = ProductListState.Error(result.message)
                }
                is ApiResult.Success ->  {
                    _state.value = ProductListState.Success(result.data)
                    isLoaded = true
                }
            }
        }
    }

    internal fun onEvent(event: ProductListEvent) {
        when (event) {
            is ProductListEvent.LoadProducts -> {
                fetchAllProducts()
            }
            is ProductListEvent.OnProductClick -> {
                event.navigateToDetails(event.id)
            }
            is ProductListEvent.OnRetry -> {
                fetchAllProducts()
            }
        }
    }
}