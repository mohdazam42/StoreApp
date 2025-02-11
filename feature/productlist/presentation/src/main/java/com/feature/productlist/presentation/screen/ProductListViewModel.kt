package com.feature.productlist.presentation.screen

import androidx.lifecycle.viewModelScope
import com.example.common.base.ApiResult
import com.example.common.base.BaseViewModel
import com.feature.productlist.domain.usecase.GetProductListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val getProductListUseCase: GetProductListUseCase) :
    BaseViewModel<ProductListEvent, ProductListState, ProductListSideEffect>() {

    private fun fetchAllProducts() {
        viewModelScope.launch {
            onLoading(loading = true)
            getProductListUseCase.invoke().collectLatest { result ->
                when (result) {
                    is ApiResult.Loading -> onLoading(loading = true)

                    is ApiResult.Success -> updateState {
                        it.copy(isLoading = false, productList = result.data)
                    }

                    is ApiResult.Error -> updateState {
                        it.copy(isLoading = false, error = result.message)
                    }
                }
            }
        }
    }

    override fun initState(): ProductListState = ProductListState(
        isLoading = true,
        productList = emptyList(),
        error = ""
    )

    override fun onLoading(loading: Boolean) {
        updateState { it.copy(isLoading = loading) }
    }

    override fun sendSideEffect(sideEffect: ProductListSideEffect) {
        viewModelScope.launch {
            sideEffectFlow.send(element = sideEffect)
        }
    }

    override fun onEvent(event: ProductListEvent) {
        when (event) {
            ProductListEvent.LoadProducts -> fetchAllProducts()
        }
    }
}