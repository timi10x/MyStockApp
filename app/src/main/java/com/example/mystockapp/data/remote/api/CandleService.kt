package com.example.mystockapp.data.remote.api

import com.example.mystockapp.domain.model.Candle
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CandleService {
    @GET("stock/candle")
    suspend fun getCandles(
        @Query("symbol") symbol: String,
        @Query("resolution") resolution: String,
        @Query("from") from: Long,
        @Query("to") to: Long,
    ) : Response<Candle>
}