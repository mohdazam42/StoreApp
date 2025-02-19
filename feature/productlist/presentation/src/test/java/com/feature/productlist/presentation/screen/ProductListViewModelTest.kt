package com.feature.productlist.presentation.screen

import com.example.common.base.ApiResult
import com.example.common.extensions.SingleValueCallback
import com.feature.productlist.domain.model.Product
import com.feature.productlist.domain.model.Rating
import com.feature.productlist.domain.usecase.GetProductListUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val getProductListUseCase: GetProductListUseCase = mockk()
    private lateinit var viewModel: ProductListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProductListViewModel(getProductListUseCase)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `Test Fetch All Products Success`() = runTest {
        // Given
        val mockProductList = listOf(
            Product(
                id = 1,
                title = "Product 1",
                price = 10.0,
                image = "image1.jpg",
                rating = Rating(rate = 4.5, count = 100)
            ),
            Product(
                id = 2,
                title = "Product 2",
                price = 20.0,
                image = "image2.jpg",
                rating = Rating(rate = 3.5, count = 50)
            )
        )
        coEvery { getProductListUseCase.invoke() } returns ApiResult.Success(mockProductList)

        // When
        viewModel.onEvent(ProductListEvent.LoadProducts)

        // Then
        val result = viewModel.state.value
        expectThat(result).isA<ProductListState.Success>()
        expectThat((result as ProductListState.Success).productList).isEqualTo(mockProductList)
        coVerify(exactly = 1) { getProductListUseCase.invoke() }
    }

    @Test
    fun `Test Fetch All Products Error`() = runTest {
        // Given
        val mockErrorMessage = "Network Error"
        coEvery { getProductListUseCase.invoke() } returns ApiResult.Error(mockErrorMessage)

        // When
        viewModel.onEvent(ProductListEvent.LoadProducts)

        // Then
        val result = viewModel.state.value
        expectThat(result).isA<ProductListState.Error>()
        expectThat((result as ProductListState.Error).message).isEqualTo(mockErrorMessage)
        coVerify(exactly = 1) { getProductListUseCase.invoke() }
    }

    @Test
    fun `Test OnEvent OnProductClick`() = runTest {
        // Given
        val productId = 1
        val navigateToDetails: SingleValueCallback<Int> = mockk(relaxed = true)
        val event = ProductListEvent.OnProductClick(productId, navigateToDetails)

        // When
        viewModel.onEvent(event)

        // Then
        verify(exactly = 1) { navigateToDetails.invoke(productId) }
    }
}
