package com.example.common.base

sealed class ApiResult<T> {
    class Loading<T> : ApiResult<T>()
    class Success<T>(val data: T) : ApiResult<T>()
    class Error<T>(val message: String) : ApiResult<T>()
}