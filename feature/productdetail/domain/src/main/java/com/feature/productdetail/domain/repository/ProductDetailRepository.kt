package com.feature.productdetail.domain.repository

import com.example.common.base.ApiResult
import com.feature.productdetail.domain.model.Product

interface ProductDetailRepository {
    suspend fun getProduct(productId: Int): ApiResult<Product>
}