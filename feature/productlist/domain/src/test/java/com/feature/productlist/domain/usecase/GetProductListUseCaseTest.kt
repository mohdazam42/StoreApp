package com.feature.productlist.domain.usecase

import com.example.common.base.ApiResult
import com.feature.productlist.domain.model.Product
import com.feature.productlist.domain.model.Rating
import com.feature.productlist.domain.repository.ProductListRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class GetProductListUseCaseTest {

    private lateinit var useCase: GetProductListUseCase
    private val mockRepository: ProductListRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetProductListUseCase(mockRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `Given repository returns data successfully When invoke is called Then emit Success`() =
        runTest {
            // Given
            val mockProducts =
                listOf(
                    Product(
                        1,
                        "Product 1",
                        10.5,
                        "https://example.com/image.png",
                        Rating(4.5, 10)
                    )
                )
            coEvery { mockRepository.getAllProducts() } returns flowOf(
                ApiResult.Success(
                    mockProducts
                )
            )

            // When
            val result = useCase().first()

            // Then
            expectThat(result).isA<ApiResult.Success<List<Product>>>()
            expectThat((result as ApiResult.Success).data.first().id).isEqualTo(1)

            coVerify(exactly = 1) { mockRepository.getAllProducts() }
        }

    @Test
    fun `Given repository fails When invoke is called Then emit Error`() = runTest {
        // Given
        val exception = RuntimeException("Repository Error")
        coEvery { mockRepository.getAllProducts() } returns flowOf(
            ApiResult.Error(
                exception.message ?: "Repository Error"
            )
        )

        // When
        val result = useCase().first()

        // Then
        expectThat(result).isA<ApiResult.Error>()
        expectThat((result as ApiResult.Error).message).isEqualTo(exception.message)

        coVerify(exactly = 1) { mockRepository.getAllProducts() }
    }
}
