package com.feature.productlist.data.repository

import com.example.common.base.ApiResult
import com.example.network.utils.safeApiCall
import com.feature.productlist.data.mappers.toDomain
import com.feature.productlist.data.remote.ProductListApiService
import com.feature.productlist.domain.model.Product
import com.feature.productlist.domain.repository.ProductListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductListRepositoryImpl @Inject constructor(private val productListApiService: ProductListApiService) :
    ProductListRepository {

    override suspend fun getAllProducts(): Flow<ApiResult<List<Product>>> = safeApiCall(
        apiCall = { productListApiService.getAllProducts() },
        mapper = { productDtoList -> productDtoList.map { it.toDomain() } }
    )
}