package com.feature.productlist.domain.usecase

import com.example.common.base.ApiResult
import com.feature.productlist.domain.model.Product
import com.feature.productlist.domain.repository.ProductListRepository
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(private val productListRepository: ProductListRepository) {

    suspend operator fun invoke(): ApiResult<List<Product>> =
        productListRepository.getAllProducts()
}