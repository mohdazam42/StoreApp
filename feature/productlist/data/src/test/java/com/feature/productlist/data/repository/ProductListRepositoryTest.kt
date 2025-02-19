package com.feature.productlist.data.repository

import com.example.common.base.ApiResult
import com.feature.productlist.data.model.ProductDto
import com.feature.productlist.data.model.RatingDto
import com.feature.productlist.data.remote.ProductListApiService
import com.feature.productlist.domain.model.Product
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class ProductListRepositoryTest {

    private lateinit var repository: ProductListRepositoryImpl
    private val mockApiService: ProductListApiService = mockk()

    @Before
    fun setUp() {
        repository = ProductListRepositoryImpl(mockApiService)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `Given API call is successful When getAllProducts is called and return Success`() =
        runTest {
            // Given
            val mockProductDtos = listOf(
                ProductDto(
                    1,
                    "Product 1",
                    10.5,
                    "Sample description",
                    "Electronics",
                    "https://example.com/image.png",
                    RatingDto(4.5, 10)
                )
            )

            coEvery { mockApiService.getAllProducts() } returns mockProductDtos

            // When
            val result = repository.getAllProducts()

            // Then
            expectThat(result).isA<ApiResult.Success<List<Product>>>()
            val successResult = result as ApiResult.Success
            expectThat(successResult.data).hasSize(1)
            expectThat(successResult.data[0].id).isEqualTo(1)
            expectThat(successResult.data[0].title).isEqualTo("Product 1")
            expectThat(successResult.data[0].price).isEqualTo(10.5)

            coVerify(exactly = 1) { mockApiService.getAllProducts() }
        }

    @Test
    fun `Given API call fails When getAllProducts is called and return Error`() =
        runTest {
            // Given
            val exception = RuntimeException("Network Error")
            coEvery { mockApiService.getAllProducts() } throws exception

            // When
            val result = repository.getAllProducts()

            // Then
            expectThat(result).isA<ApiResult.Error>()
            val errorResult = result as ApiResult.Error
            expectThat(errorResult.message).isEqualTo(exception.message)

            coVerify(exactly = 1) { mockApiService.getAllProducts() }
        }
}
