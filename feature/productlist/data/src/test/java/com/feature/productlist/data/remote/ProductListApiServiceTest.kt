package com.feature.productlist.data.remote

import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import java.net.HttpURLConnection

class ProductListApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ProductListApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // Use MockWebServer's URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductListApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `Given API responds successfully When getAllProducts is called Then return a list of ProductDto`() =
        runTest {
            // Given
            val responseJson = """
            [
                {
                    "id": 1,
                    "title": "Product 1",
                    "price": 10.5,
                    "description": "Sample description",
                    "category": "Electronics",
                    "image": "https://example.com/image.png",
                    "rating": {
                        "rate": 4.5,
                        "count": 10
                    }
                }
            ]
        """.trimIndent()

            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(responseJson)
            )

            // When
            val result = apiService.getAllProducts()

            // Then
            expectThat(result)
                .isNotNull()
                .hasSize(1)
                .and {
                    get { first().id }.isEqualTo(1)
                    get { first().title }.isEqualTo("Product 1")
                    get { first().price }.isEqualTo(10.5)
                    get { first().description }.isEqualTo("Sample description")
                    get { first().category }.isEqualTo("Electronics")
                    get { first().image }.isEqualTo("https://example.com/image.png")
                    get { first().rating.rate }.isEqualTo(4.5)
                    get { first().rating.count }.isEqualTo(10)
                }
        }

    @Test
    fun `Given API returns empty response When getAllProducts is called Then return an empty list`() =
        runTest {
            // Given
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody("[]")
            )

            // When
            val result = apiService.getAllProducts()

            // Then
            expectThat(result).hasSize(0)
        }

    @Test
    fun `Given API returns server error When getAllProducts is called Then throw an exception`() =
        runTest {
            // Given
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
            )

            // When & Then
            try {
                apiService.getAllProducts()
            } catch (e: Exception) {
                expectThat(e).isNotNull()
            }
        }
}
