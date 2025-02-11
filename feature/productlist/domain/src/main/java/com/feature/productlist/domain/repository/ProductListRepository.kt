package com.feature.productlist.domain.repository

import com.example.common.base.ApiResult
import com.feature.productlist.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductListRepository {
    suspend fun getAllProducts(): Flow<ApiResult<List<Product>>>
}