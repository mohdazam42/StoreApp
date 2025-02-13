package com.feature.productlist.domain.repository

import com.example.common.base.ApiResult
import com.feature.productlist.domain.model.Product

interface ProductListRepository {
    suspend fun getAllProducts(): ApiResult<List<Product>>
}