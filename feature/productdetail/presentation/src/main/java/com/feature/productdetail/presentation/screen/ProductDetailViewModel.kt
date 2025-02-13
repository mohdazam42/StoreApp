package com.feature.productdetail.presentation.screen

import androidx.lifecycle.viewModelScope
import com.example.common.base.ApiResult
import com.example.common.base.BaseViewModel
import com.feature.productdetail.domain.usecase.GetProductDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val getProductDetailUseCase: GetProductDetailUseCase) :
    BaseViewModel<ProductDetailEvent, ProductDetailState>() {

    private fun fetchProduct(productId: Int) {
        updateState()
        viewModelScope.launch {
            when (val result = getProductDetailUseCase.invoke(productId)) {
                is ApiResult.Error -> updateState {
                    it.copy(isLoading = false, error = result.message)
                }
                is ApiResult.Success -> updateState {
                    it.copy(isLoading = false, product = result.data)
                }
            }
        }
    }

    private fun updateState() {
        updateState { it.copy(isLoading = true) }
    }

    override fun initState(): ProductDetailState = ProductDetailState()

    override fun onEvent(event: ProductDetailEvent) {
        when (event) {
            is ProductDetailEvent.LoadProduct -> fetchProduct(event.productId)
            is ProductDetailEvent.OnNavigateBack -> {
                event.navigateBack()
            }
        }
    }
}