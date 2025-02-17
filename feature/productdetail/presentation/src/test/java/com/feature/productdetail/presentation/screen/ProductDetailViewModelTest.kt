package com.feature.productdetail.presentation.screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.common.base.ApiResult
import com.feature.productdetail.domain.model.Product
import com.feature.productdetail.domain.model.Rating
import com.feature.productdetail.domain.usecase.GetProductDetailUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
    private val getProductDetailUseCase: GetProductDetailUseCase = mockk()
    private lateinit var viewModel: ProductDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProductDetailViewModel(getProductDetailUseCase)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `Test Fetch Product Success`() = runTest {
        // Given
        val mockProduct = Product(
            id = 1,
            title = "Test Product",
            price = 99.99,
            image = "test_image.jpg",
            rating = Rating(rate = 4.5, count = 100),
            description = "Test Description",
            category = "Test Category"
        )
        coEvery { getProductDetailUseCase.invoke(mockProduct.id) } returns ApiResult.Success(mockProduct)

        // When
        viewModel.onEvent(ProductDetailEvent.LoadProduct(mockProduct.id))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        expectThat(viewModel.state.value) {
            get { this is ProductDetailState.Success }.isTrue()
            get { (this as ProductDetailState.Success).product }.isEqualTo(mockProduct)
        }
        coVerify(exactly = 1) { getProductDetailUseCase.invoke(mockProduct.id) }
    }

    @Test
    fun `Test Fetch Product Error`() = runTest {
        // Given
        val productId = 1
        val errorMessage = "Product not found"
        coEvery { getProductDetailUseCase.invoke(productId) } returns ApiResult.Error(errorMessage)

        // When
        viewModel.onEvent(ProductDetailEvent.LoadProduct(productId))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        expectThat(viewModel.state.value) {
            get { this is ProductDetailState.Error }.isTrue()
            get { (this as ProductDetailState.Error).message }.isEqualTo(errorMessage)
        }
        coVerify(exactly = 1) { getProductDetailUseCase.invoke(productId) }
    }

    @Test
    fun `Test OnEvent LoadProduct`() = runTest {
        // Given
        val productId = 1
        coEvery { getProductDetailUseCase.invoke(productId) } returns ApiResult.Success(
            Product(
                id = productId,
                title = "Test Product",
                price = 99.99,
                image = "test_image.jpg",
                rating = Rating(rate = 4.5, count = 100),
                description = "Test Description",
                category = "Test Category"
            )
        )

        // When
        viewModel.onEvent(ProductDetailEvent.LoadProduct(productId))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { getProductDetailUseCase.invoke(productId) }
    }

    @Test
    fun `Test OnEvent OnNavigateBack`() = runTest {
        // Given
        val navigateBack: () -> Unit = mockk(relaxed = true)
        val event = ProductDetailEvent.OnNavigateBack(navigateBack)

        // When
        viewModel.onEvent(event)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(exactly = 1) { navigateBack() }
    }
}