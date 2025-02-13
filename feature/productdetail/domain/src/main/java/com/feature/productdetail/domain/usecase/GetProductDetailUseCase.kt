package com.feature.productdetail.domain.usecase

import com.example.common.base.ApiResult
import com.feature.productdetail.domain.model.Product
import com.feature.productdetail.domain.repository.ProductDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(private val productDetailRepository: ProductDetailRepository) {

    suspend operator fun invoke(productId: Int): ApiResult<Product> =
        productDetailRepository.getProduct(productId)
}