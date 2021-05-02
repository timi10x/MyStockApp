package com.example.mystockapp.data.repository

import com.example.mystockapp.core.Result
import com.example.mystockapp.data.remote.api.CandleService
import com.example.mystockapp.data.remote.api.CompaniesService
import com.example.mystockapp.data.remote.api.QuoteService
import com.example.mystockapp.data.toDomainFlow
import com.example.mystockapp.domain.model.Candle
import com.example.mystockapp.domain.model.CompanyProfile
import com.example.mystockapp.domain.model.Quote
import com.example.mystockapp.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val candleService: CandleService,
    private val companiesService: CompaniesService,
    private val quoteService: QuoteService
): StockRepository {
    override suspend fun getCompanies(): Flow<Result<List<CompanyProfile>>> {
        return companiesService.getCompanies().toDomainFlow()
    }

    override suspend fun getQuote(ticker: String): Flow<Result<Quote>> {
        return quoteService.getQuote(ticker).toDomainFlow()
    }

    override suspend fun getCandles(
        symbol: String,
        resolution: String,
        from: Long,
        to: Long
    ): Flow<Result<Candle>> {
        return candleService.getCandles(
            symbol,resolution, from, to
        ).toDomainFlow()
    }
}