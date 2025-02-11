package com.feature.productdetail.data.remote

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import java.net.HttpURLConnection

class ProductDetailApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ProductDetailApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(path = "/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductDetailApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Given API responds successfully When getProduct is called Then return ProductDto`() =
        kotlinx.coroutines.test.runTest {
            // Given
            val responseJson = """
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
        """.trimIndent()

            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(responseJson)
            )

            // When
            val result = apiService.getProduct(productId = 1)

            // Then
            expectThat(result).isNotNull()
            expectThat(result.id).isEqualTo(1)
            expectThat(result.title).isEqualTo("Product 1")
            expectThat(result.price).isEqualTo(10.5)
        }

    @Test
    fun `Given API returns error response When getProduct is called Then throw an exception`() =
        kotlinx.coroutines.test.runTest {
            // Given
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
            )

            // When & Then
            try {
                apiService.getProduct(productId = 1)
            } catch (e: Exception) {
                expectThat(e).isNotNull()
            }
        }
}
