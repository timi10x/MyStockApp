package com.example.mystockapp.core.di


import com.example.mystockapp.BuildConfig
import com.example.mystockapp.data.remote.api.CandleService
import com.example.mystockapp.data.remote.api.CompaniesService
import com.example.mystockapp.data.remote.api.QuoteService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    var candleApiClient: NetworkModule? = null

    fun getClient(): NetworkModule? {
        if (candleApiClient == null) {
            candleApiClient = NetworkModule
        }
        return candleApiClient
    }
    private val requestInterceptor = Interceptor { chain ->
        val url = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("token", BuildConfig.FinnApiKey)
                .build()
        val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

        return@Interceptor chain.proceed(request)
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .build()

    val retrofitCandleInstance: CandleService
        get() {
            val retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(CandleService::class.java)
        }

    private val requestClientInterceptor = Interceptor { chain ->
        val url = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("token", BuildConfig.IexKey)
                .build()
        val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

        return@Interceptor chain.proceed(request)
    }

    private val okHttpQuoteClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestClientInterceptor)
            .build()


    val retrofitQuoteInstance: QuoteService
        get() {
            val retrofit = Retrofit.Builder()
                    .client(okHttpQuoteClient)
                    .baseUrl(BuildConfig.IEX_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(QuoteService::class.java)
        }

    val retrofitCompaniesInstance: CompaniesService
        get() {
            val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.Github_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(CompaniesService::class.java)
        }

}
