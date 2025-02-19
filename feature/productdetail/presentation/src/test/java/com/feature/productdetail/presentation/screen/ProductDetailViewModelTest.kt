package com.feature.productdetail.presentation.screen

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
class ProductDetailViewModelTest {

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
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
    fun `Test Fetch Product Success`() = runTest(testDispatcher) {
        // Given
        val mockProduct = Product(
            id = 1,
            title = "Product title",
            price = 99.99,
            image = "image.jpg",
            rating = Rating(rate = 4.5, count = 100),
            description = "Test description",
            category = "Test category"
        )
        coEvery { getProductDetailUseCase.invoke(mockProduct.id) } returns ApiResult.Success(mockProduct)

        // When
        viewModel.onEvent(ProductDetailEvent.LoadProduct(mockProduct.id))

        // Then
        val result = viewModel.state.value
        expectThat(result).isA<ProductDetailState.Success>()
        expectThat((result as ProductDetailState.Success).product).isEqualTo(mockProduct)
        coVerify(exactly = 1) { getProductDetailUseCase.invoke(mockProduct.id) }
    }

    @Test
    fun `Test Fetch Product Error`() = runTest(testDispatcher) {
        // Given
        val productId = 1
        val errorMessage = "Product not found"
        coEvery { getProductDetailUseCase.invoke(productId) } returns ApiResult.Error(errorMessage)

        // When
        viewModel.onEvent(ProductDetailEvent.LoadProduct(productId))

        // Then
        val result = viewModel.state.value
        expectThat(result).isA<ProductDetailState.Error>()
        expectThat((result as ProductDetailState.Error).message).isEqualTo(errorMessage)
        coVerify(exactly = 1) { getProductDetailUseCase.invoke(productId) }
    }

    @Test
    fun `Test OnEvent LoadProduct`() = runTest(testDispatcher) {
        // Given
        val productId = 1
        coEvery { getProductDetailUseCase.invoke(productId) } returns ApiResult.Success(
            Product(
                id = productId,
                title = "Product title",
                price = 99.99,
                image = "image.jpg",
                rating = Rating(rate = 4.5, count = 100),
                description = "Test description",
                category = "Test category"
            )
        )

        // When
        viewModel.onEvent(ProductDetailEvent.LoadProduct(productId))

        // Then
        coVerify(exactly = 1) { getProductDetailUseCase.invoke(productId) }
    }

    @Test
    fun `Test OnEvent OnNavigateBack`() = runTest(testDispatcher) {
        // Given
        val navigateBack: () -> Unit = mockk(relaxed = true)
        val event = ProductDetailEvent.OnNavigateBack(navigateBack)

        // When
        viewModel.onEvent(event)

        // Then
        verify(exactly = 1) { navigateBack() }
    }

    @Test
    fun `Test OnRetry event triggers fetch all products and returns success`() = runTest(testDispatcher) {
        // Given
        val mockProduct = Product(
            id = 1,
            title = "Product title",
            price = 99.99,
            image = "image.jpg",
            rating = Rating(rate = 4.5, count = 100),
            description = "Test description",
            category = "Test category"
        )
        coEvery { getProductDetailUseCase.invoke(mockProduct.id) } returns ApiResult.Success(mockProduct)

        // When
        viewModel.onEvent(ProductDetailEvent.OnRetry(productId = mockProduct.id))

        // Then
        val result = viewModel.state.value
        expectThat(result).isA<ProductDetailState.Success>()
        expectThat((result as ProductDetailState.Success).product).isEqualTo(mockProduct)

        // Verify
        coVerify(exactly = 1) { getProductDetailUseCase.invoke(mockProduct.id) }
    }

    @Test
    fun `Test OnRetry event triggers fetch all products and returns error`() = runTest(testDispatcher) {
        // Given
        val productId = 1
        val mockErrorMsg = "Network Error"
        coEvery { getProductDetailUseCase.invoke(productId) } returns ApiResult.Error(mockErrorMsg)

        // When
        viewModel.onEvent(ProductDetailEvent.OnRetry(productId))

        // Then
        val result = viewModel.state.value
        expectThat(result).isA<ProductDetailState.Error>()
        expectThat((result as ProductDetailState.Error).message).isEqualTo(mockErrorMsg)

        coVerify(exactly = 1) { getProductDetailUseCase.invoke(productId) }
    }
}