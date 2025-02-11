package com.example.network.utils

import com.example.common.base.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

fun <T, R> safeApiCall(
    apiCall: suspend () -> T,
    mapper: (T) -> R
): Flow<ApiResult<R>> = flow {
    try {
        // loading
        emit(ApiResult.Loading())
        //network call
        val response = withContext(Dispatchers.IO) { apiCall() }
        // Emit success
        emit(ApiResult.Success(mapper(response)))
    } catch (e: Exception) {
        // Catch the error
        emit(ApiResult.Error(message = e.message ?: "Unknown error"))
    }
}.flowOn(Dispatchers.IO)
