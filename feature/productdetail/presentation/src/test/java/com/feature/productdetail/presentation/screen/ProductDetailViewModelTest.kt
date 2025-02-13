package com.feature.productdetail.presentation.screen

import app.cash.turbine.test
import com.example.common.base.ApiResult
import com.feature.productdetail.domain.model.Product
import com.feature.productdetail.domain.model.Rating
import com.feature.productdetail.domain.usecase.GetProductDetailUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ProductDetailViewModelTest {

    private lateinit var viewModel: ProductDetailViewModel
    private val mockUseCase: GetProductDetailUseCase = mockk()

    @Before
    fun setUp() {
        viewModel = ProductDetailViewModel(mockUseCase)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `Given repository emits Success When fetchProduct is called Then emit Loading first then Success`() =
        runUnconfinedTest {
            // Given
            val mockProduct = Product(
                id = 1,
                title = "Product 1",
                price = 10.5,
                image = "https://example.com/image.png",
                rating = Rating(4.5, 10),
                description = "description",
                category = "category"
            )
            coEvery { mockUseCase.invoke(productId = 1) } returns flowOf(
                ApiResult.Success(mockProduct)
            )

            // When
            viewModel.onEvent(ProductDetailEvent.LoadProduct(productId = 1))

            // Then
            viewModel.state.test {
                expectThat(awaitItem().isLoading).isEqualTo(true)
                val successState = awaitItem()
                expectThat(successState.isLoading).isEqualTo(false)
                expectThat(successState.product).isEqualTo(mockProduct)

                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) { mockUseCase.invoke(productId = 1) }
        }

    @Test
    fun `Given repository emits Error When fetchProduct is called Then emit Loading first then Error`() =
        runUnconfinedTest {
            // Arrange
            val exceptionMessage = "Network Error"
            coEvery { mockUseCase.invoke(1) } returns flowOf(
                ApiResult.Error(exceptionMessage)
            )

            // Act
            viewModel.onEvent(ProductDetailEvent.LoadProduct(1))

            // Assert
            viewModel.state.test {
                expectThat(awaitItem().isLoading).isEqualTo(true)
                val errorState = awaitItem()
                expectThat(errorState.isLoading).isEqualTo(false)
                expectThat(errorState.error).isEqualTo(exceptionMessage)

                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) { mockUseCase.invoke(productId = 1) }
        }
}
