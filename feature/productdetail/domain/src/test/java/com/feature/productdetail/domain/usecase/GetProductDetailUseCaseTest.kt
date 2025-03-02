package com.feature.productdetail.domain.usecase

import com.example.common.base.ApiResult
import com.feature.productdetail.domain.model.Product
import com.feature.productdetail.domain.model.Rating
import com.feature.productdetail.domain.repository.ProductDetailRepository
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
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class GetProductDetailUseCaseTest {

    private lateinit var useCase: GetProductDetailUseCase
    private val mockRepository: ProductDetailRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetProductDetailUseCase(mockRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `Given repository returns data successfully When invoke is called Then emit Loading first then Success`() =
        runTest {
            // Given
            val mockProduct = Product(
                id = 1,
                title = "Product 1",
                price = 10.5,
                image = "https://example.com/image.png",
                rating = Rating(4.5, 10),
                description = "",
                category = ""
            )

            coEvery { mockRepository.getProduct(1) } returns ApiResult.Success(mockProduct)

            // When
            val result = useCase(1)

            // Then
            expectThat(result).isA<ApiResult.Success<Product>>()
            val successResult = result as ApiResult.Success
            expectThat(successResult.data.id).isEqualTo(1)
            expectThat(successResult.data.title).isEqualTo("Product 1")

            coVerify(exactly = 1) { mockRepository.getProduct(1) }
        }

    @Test
    fun `Given repository fails When invoke is called Then emit Loading first then Error`() =
        runTest {
            // Given
            val exception = RuntimeException("Repository Error")
            coEvery { mockRepository.getProduct(1) } returns ApiResult.Error(exception.message ?: "Repository Error")

            // When
            val result = useCase(1)

            // Then
            expectThat(result).isA<ApiResult.Error>()
            val errorResult = result as ApiResult.Error
            expectThat(errorResult.message).isEqualTo(exception.message)

            coVerify(exactly = 1) { mockRepository.getProduct(1) }
        }
}
