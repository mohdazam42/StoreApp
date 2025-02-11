package com.feature.productdetail.data.remote

import com.feature.productdetail.data.model.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductDetailApiService {

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") productId: Int
    ): ProductDto
}