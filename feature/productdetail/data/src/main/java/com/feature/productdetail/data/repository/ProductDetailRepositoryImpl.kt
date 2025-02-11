package com.feature.productdetail.data.repository

import com.example.common.base.ApiResult
import com.example.network.utils.safeApiCall
import com.feature.productdetail.data.mappers.toDomain
import com.feature.productdetail.data.remote.ProductDetailApiService
import com.feature.productdetail.domain.model.Product
import com.feature.productdetail.domain.repository.ProductDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductDetailRepositoryImpl
@Inject constructor(
    private val productDetailApiService: ProductDetailApiService
) : ProductDetailRepository {
    override suspend fun getProduct(productId: Int): Flow<ApiResult<Product>> = safeApiCall(
        apiCall = { productDetailApiService.getProduct(productId) },
        mapper = { it.toDomain() }
    )
}