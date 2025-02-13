package com.feature.productlist.presentation.screen

import app.cash.turbine.test
import com.example.common.base.ApiResult
import com.feature.productlist.domain.model.Product
import com.feature.productlist.domain.model.Rating
import com.feature.productlist.domain.usecase.GetProductListUseCase
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

class ProductListViewModelTest {

    private lateinit var viewModel: ProductListViewModel
    private val mockUseCase: GetProductListUseCase = mockk()

    @Before
    fun setUp() {
        viewModel = ProductListViewModel(mockUseCase)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `Given repository emits Success When fetchAllProducts is called Then emit Loading first then Success`() =
        runUnconfinedTest {
            // Given
            val mockProducts = listOf(
                Product(
                    id = 1,
                    title = "Product 1",
                    price = 10.5,
                    image = "image_url",
                    rating = Rating(rate = 4.5, count = 10)
                )
            )
            coEvery { mockUseCase.invoke() } returns flowOf(
                ApiResult.Success(mockProducts)
            )

            // When
            viewModel.onEvent(ProductListEvent.LoadProducts)

            // Then
            viewModel.state.test {
                expectThat(awaitItem().isLoading).isEqualTo(true) // Loading state
                val successState = awaitItem()
                expectThat(successState.isLoading).isEqualTo(false)
                expectThat(successState.productList).isEqualTo(mockProducts)
                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) { mockUseCase.invoke() }
        }

    @Test
    fun `Given repository emits Error When fetchAllProducts is called Then emit Loading first then Error`() =
        runUnconfinedTest {
            // Given
            val exceptionMessage = "Network Error"
            coEvery { mockUseCase.invoke() } returns flowOf(
                ApiResult.Error(exceptionMessage)
            )

            // When
            viewModel.onEvent(ProductListEvent.LoadProducts)

            // Then
            viewModel.state.test {
                expectThat(awaitItem().isLoading).isEqualTo(true) // Loading state
                val errorState = awaitItem()
                expectThat(errorState.isLoading).isEqualTo(false)
                expectThat(errorState.error).isEqualTo(exceptionMessage)
                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) { mockUseCase.invoke() }
        }
}

