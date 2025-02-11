package com.feature.productlist.data.remote

import com.feature.productlist.data.model.ProductDto
import retrofit2.http.GET

interface ProductListApiService {

    @GET("products")
    suspend fun getAllProducts(): List<ProductDto>
}