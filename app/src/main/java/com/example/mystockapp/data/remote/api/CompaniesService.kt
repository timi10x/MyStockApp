package com.example.mystockapp.data.remote.api

import com.example.mystockapp.domain.model.CompanyProfile
import retrofit2.Response
import retrofit2.http.GET

interface CompaniesService {
    @GET("timmyCoder/listing/main/listing.json")
    suspend fun getCompanies(): Response<List<CompanyProfile>>
}