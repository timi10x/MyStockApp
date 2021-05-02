package com.example.mystockapp.core.di

import android.content.Context
import androidx.room.Room
import com.example.mystockapp.BuildConfig
import com.example.mystockapp.data.local.StockDatabase
import com.example.mystockapp.data.remote.api.CandleService
import com.example.mystockapp.data.remote.api.CompaniesService
import com.example.mystockapp.data.remote.api.QuoteService
import com.example.mystockapp.domain.model.Quote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitCompanies(): Retrofit{
        return Retrofit.Builder()
                .baseUrl(BuildConfig.Github_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
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

    @Provides
    @Singleton
    fun provideRetrofitQuotes(): Retrofit{
        return Retrofit.Builder()
                .baseUrl(BuildConfig.IEX_URL)
                .client(okHttpQuoteClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }


    @Singleton
    @Provides
    fun providesCandleService(retrofit: Retrofit): CandleService {
        return retrofit.create(CandleService::class.java)
    }

    @Singleton
    @Provides
    fun providesCompaniesService(retrofit: Retrofit): CompaniesService {
        return retrofit.create(CompaniesService::class.java)
    }

    @Singleton
    @Provides
    fun providesQuoteServices(retrofit: Retrofit): QuoteService {
        return retrofit.create(QuoteService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun providesDataBase(@ApplicationContext context: Context): StockDatabase {
        return Room.databaseBuilder(
                context.applicationContext,
                StockDatabase::class.java,
                StockDatabase.DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesCompanyDao(db: StockDatabase) = db.companyDao()
}