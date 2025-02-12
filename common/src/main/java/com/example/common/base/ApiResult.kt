package com.example.common.base

sealed class ApiResult<out T> {
    data object Loading: ApiResult<Nothing>()
    class Success<T>(val data: T) : ApiResult<T>()
    class Error(val message: String) : ApiResult<Nothing>()
}