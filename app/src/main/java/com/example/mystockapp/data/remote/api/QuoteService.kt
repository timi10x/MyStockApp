package com.example.mystockapp.data.remote.api

import com.example.mystockapp.domain.model.Quote
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface QuoteService {
    @GET("stock/{symbol}/quote")
    fun getQuote(@Path("symbol") ticker: String): Observable<Quote>
}