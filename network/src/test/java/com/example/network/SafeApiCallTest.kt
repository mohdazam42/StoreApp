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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
class SafeApiCallTest {

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val mockApiCall: suspend () -> String = mockk()
    private val mockMapper: (String) -> Int = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `Given a successful API call When safeApiCall is executed Then emit Success`() =
        runTest(testDispatcher) {
            // Given
            val apiResponse = "Success Response"
            val mappedValue = 42

            // Mock the behavior of apiCall and mapper
            coEvery { mockApiCall() } returns apiResponse
            every { mockMapper(apiResponse) } returns mappedValue

            // When
            val result = safeApiCall(mockApiCall, mockMapper)

            // Then
            expectThat(result).isA<ApiResult.Success<Int>>()
            expectThat((result as ApiResult.Success).data).isEqualTo(mappedValue)
            coVerify(exactly = 1) { mockApiCall() }
            verify(exactly = 1) { mockMapper(apiResponse) }
        }

    @Test
    fun `Given an API call failure When safeApiCall is executed Then emit Error`() =
        runTest(testDispatcher) {
            // Given
            val exception = RuntimeException("Network Error")

            // Mock the behavior of apiCall throwing an exception
            coEvery { mockApiCall() } throws exception

            // When
            val result = safeApiCall(mockApiCall, mockMapper)

            // Then
            expectThat(result).isA<ApiResult.Error>()
            expectThat((result as ApiResult.Error).message).isEqualTo("Network Error")
            coVerify(exactly = 1) { mockApiCall() }
            verify(exactly = 0) { mockMapper(any()) }
        }

    @Test
    fun `Given a successful API call When safeApiCall is executed Then it runs on Dispatchers IO`() =
        runTest(testDispatcher) {
            // Given
            val apiResponse = "Success Response"
            val mappedValue = 42

            // Mock the behavior of apiCall running on Dispatchers.IO
            coEvery { mockApiCall() } coAnswers {
                withContext(Dispatchers.IO) { apiResponse }
            }
            every { mockMapper(apiResponse) } returns mappedValue

            // When
            val result = safeApiCall(mockApiCall, mockMapper)

            // Then
            expectThat(result).isA<ApiResult.Success<Int>>()
            coVerify(exactly = 1) { mockApiCall() }
            verify(exactly = 1) { mockMapper(apiResponse) }
        }
}
