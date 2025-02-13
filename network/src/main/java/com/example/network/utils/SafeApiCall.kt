package com.example.network.utils

import com.example.common.base.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T, R> safeApiCall(
    apiCall: suspend () -> T,
    mapper: (T) -> R
): ApiResult<R> {
    return try {
        //network call
        val response = withContext(Dispatchers.IO) { apiCall() }
        // Emit success
        ApiResult.Success(mapper(response))
    } catch (e: Exception) {
        // Catch the error
        ApiResult.Error(message = e.message ?: "Unknown error")
    }
}
