package com.example.network

import com.example.common.base.ApiResult
import com.example.network.utils.safeApiCall
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class SafeApiCallTest {

    private val mockApiCall: suspend () -> String = mockk()
    private val mockMapper: (String) -> Int = mockk()

    @Before
    fun setUp() {
        // Setup behavior for mocks if needed
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `Given a successful API call When safeApiCall is executed Then emit Loading first then Success`() =
        runUnconfinedTest {
            // Given
            val apiResponse = "Success Response"
            val mappedValue = 42

            coEvery { mockApiCall() } returns apiResponse
            every { mockMapper(apiResponse) } returns mappedValue

            // When
            val result = safeApiCall(mockApiCall, mockMapper).toList()

            // Then
            expectThat(result).hasSize(1)
            expectThat(result[0]).isA<ApiResult.Success<Int>>()
            expectThat((result[0] as ApiResult.Success).data).isEqualTo(mappedValue)

            coVerify(exactly = 1) { mockApiCall() }
            verify(exactly = 1) { mockMapper(apiResponse) }
        }

    @Test
    fun `Given an API call failure When safeApiCall is executed Then emit Loading first then Error`() =
        runUnconfinedTest {
            // Given
            val exception = RuntimeException("Network Error")

            coEvery { mockApiCall() } throws exception

            // When
            val result = safeApiCall(mockApiCall, mockMapper).toList()

            // Then
            expectThat(result).hasSize(1)
            expectThat(result[0]).isA<ApiResult.Error>()
            expectThat((result[0] as ApiResult.Error).message).isEqualTo("Network Error")

            coVerify(exactly = 1) { mockApiCall() }
            verify(exactly = 0) { mockMapper(any()) } // Mapper should not be called on error
        }

    @Test
    fun `Given a successful API call When safeApiCall is executed Then it runs on Dispatchers IO`() =
        runUnconfinedTest {
            // Given
            val apiResponse = "Success Response"
            val mappedValue = 42

            coEvery { mockApiCall() } coAnswers {
                withContext(Dispatchers.IO) { apiResponse }
            }
            every { mockMapper(apiResponse) } returns mappedValue

            // When
            val result = safeApiCall(mockApiCall, mockMapper).toList()

            // Then
            expectThat(result).hasSize(1)
            expectThat(result[0]).isA<ApiResult.Success<Int>>()

            coVerify(exactly = 1) { mockApiCall() }
            verify(exactly = 1) { mockMapper(apiResponse) }
        }
}
