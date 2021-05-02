package com.example.mystockapp.domain.repository

import com.example.mystockapp.domain.model.Candle
import com.example.mystockapp.domain.model.CompanyProfile
import com.example.mystockapp.domain.model.Quote
import kotlinx.coroutines.flow.Flow
import com.example.mystockapp.core.Result

interface StockRepository {
    suspend fun getCompanies(): Flow<Result<List<CompanyProfile>>>
    suspend fun getQuote(ticker: String): Flow<Result<Quote>>
    suspend fun getCandles(
        symbol: String,
        resolution: String,
        from: Long,
        to: Long,
    ): Flow<Result<Candle>>
}