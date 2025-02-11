package com.feature.productdetail.data.repository

import com.example.common.base.ApiResult
import com.feature.productdetail.data.model.ProductDto
import com.feature.productdetail.data.model.RatingDto
import com.feature.productdetail.data.remote.ProductDetailApiService
import com.feature.productdetail.domain.model.Product
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class ProductDetailRepositoryTest {

    private lateinit var repository: ProductDetailRepositoryImpl
    private val mockApiService: ProductDetailApiService = mockk()

    @Before
    fun setUp() {
        repository = ProductDetailRepositoryImpl(mockApiService)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `Given API call is successful When getProduct is called Then emit Loading first then Success`() =
        runTest {
            // Given
            val mockProductDto = ProductDto(
                id = 1,
                title = "Product 1",
                price = 10.5,
                description = "Sample description",
                category = "Electronics",
                image = "https://example.com/image.png",
                rating = RatingDto(rate = 4.5, count = 10)
            )

            coEvery { mockApiService.getProduct(1) } returns mockProductDto

            // When
            val result = repository.getProduct(1).toList()

            // Then
            expectThat(result).hasSize(2)
            expectThat(result[0]).isA<ApiResult.Loading<Product>>()
            expectThat(result[1]).isA<ApiResult.Success<Product>>()

            val successResult = result[1] as ApiResult.Success
            expectThat(successResult.data.id).isEqualTo(1)
            expectThat(successResult.data.title).isEqualTo("Product 1")

            coVerify(exactly = 1) { mockApiService.getProduct(productId = 1) }
        }

    @Test
    fun `Given API call fails When getProduct is called Then emit Loading first then Error`() =
        runTest {
            // Given
            val exception = RuntimeException("Network Error")
            coEvery { mockApiService.getProduct(productId = 1) } throws exception

            // When
            val result = repository.getProduct(productId = 1).toList()

            // Then
            expectThat(result).hasSize(2)
            expectThat(result[0]).isA<ApiResult.Loading<Product>>()
            expectThat(result[1]).isA<ApiResult.Error<Product>>()
            expectThat((result[1] as ApiResult.Error).message).isEqualTo(exception.message)

            coVerify(exactly = 1) { mockApiService.getProduct(productId = 1) }
        }
}
