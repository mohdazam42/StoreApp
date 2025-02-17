package com.feature.productdetail.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.base.ApiResult
import com.feature.productdetail.domain.usecase.GetProductDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val getProductDetailUseCase: GetProductDetailUseCase) :
    ViewModel() {

    private val _state: MutableStateFlow<ProductDetailState> = MutableStateFlow(ProductDetailState.Loading)
    val state = _state.asStateFlow()
    private var isLoaded = false

    private fun fetchProduct(productId: Int) {
        if (isLoaded) return
        viewModelScope.launch {
            _state.value = ProductDetailState.Loading
            when (val result = getProductDetailUseCase.invoke(productId)) {
                is ApiResult.Error ->  {
                    _state.value = ProductDetailState.Error(result.message)
                }
                is ApiResult.Success ->  {
                    _state.value = ProductDetailState.Success(result.data)
                    isLoaded = true
                }
            }
        }
    }

    internal fun onEvent(event: ProductDetailEvent) {
        when (event) {
            is ProductDetailEvent.LoadProduct -> fetchProduct(event.productId)
            is ProductDetailEvent.OnNavigateBack -> {
                event.navigateBack()
            }
            is ProductDetailEvent.OnRetry -> {
                fetchProduct(event.productId)
            }
        }
    }
}