package com.feature.productdetail.presentation.screen

import androidx.lifecycle.viewModelScope
import com.example.common.base.ApiResult
import com.example.common.base.BaseViewModel
import com.feature.productdetail.domain.model.Product
import com.feature.productdetail.domain.model.Rating
import com.feature.productdetail.domain.usecase.GetProductDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val getProductDetailUseCase: GetProductDetailUseCase) :
    BaseViewModel<ProductDetailEvent, ProductDetailState>() {

    private fun fetchProduct(productId: Int) {
        viewModelScope.launch {
            onLoading(loading = true)
            getProductDetailUseCase.invoke(productId).collectLatest { result ->
                when (result) {
                    is ApiResult.Success -> updateState {
                        it.copy(isLoading = false, product = result.data)
                    }

                    is ApiResult.Error -> updateState {
                        it.copy(isLoading = false, error = result.message)
                    }
                }
            }
        }
    }

    override fun initState(): ProductDetailState = ProductDetailState(
        isLoading = false,
        product = Product(
            id = 0,
            title = "",
            price = 0.0,
            image = "",
            rating = Rating(
                rate = 0.0,
                count = 0
            ),
            description = "",
            category = ""
        ),
        error = ""
    )

    override fun onLoading(loading: Boolean) {
        updateState { it.copy(isLoading = loading) }
    }

    override fun onEvent(event: ProductDetailEvent) {
        when (event) {
            is ProductDetailEvent.LoadProduct -> fetchProduct(event.productId)
            is ProductDetailEvent.OnNavigateBack -> {
                event.navigateBack()
            }
        }
    }
}