package com.example.mystockapp.data

import com.example.mystockapp.core.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

fun <T : Any> Response<T>.toDomainFlow(): Flow<Result<T>> {
    return flow {
        val apiCall = this@toDomainFlow
        if (apiCall.isSuccessful && apiCall.body() != null) {
            emit(Result.Success(apiCall.body()!!))
        } else {
            emit(Result.Error(apiCall.message()))
        }
    }
}