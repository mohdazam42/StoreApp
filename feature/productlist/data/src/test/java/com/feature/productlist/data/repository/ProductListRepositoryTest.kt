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
import kotlinx.coroutines.flow.toList
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
    fun `Given API call is successful When getAllProducts is called Then emit Loading first then Success`() =
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
            val emissions = repository.getAllProducts().toList()

            // Then
            expectThat(emissions).hasSize(1)
            expectThat(emissions[0]).isA<ApiResult.Success<List<Product>>>()

            coVerify(exactly = 1) { mockApiService.getAllProducts() }
        }

    @Test
    fun `Given API call fails When getAllProducts is called Then emit Loading first then Error`() =
        runTest {
            // Given
            val exception = RuntimeException("Network Error")
            coEvery { mockApiService.getAllProducts() } throws exception

            // When
            val emissions = repository.getAllProducts().toList()

            // Then
            expectThat(emissions).hasSize(1)
            expectThat(emissions[0]).isA<ApiResult.Error>()
            expectThat((emissions[0] as ApiResult.Error).message).isEqualTo(exception.message)

            coVerify(exactly = 1) { mockApiService.getAllProducts() }
        }
}
