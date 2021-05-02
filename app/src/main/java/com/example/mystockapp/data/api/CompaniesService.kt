package com.example.mystockapp.data.api

import com.example.mystockapp.domain.model.CompanyProfile
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

interface CompaniesService {
    @GET("mbaimuratov/stockProfiles/main/stockProfiles.json")
    suspend fun getCompanies(): Response<List<CompanyProfile>>
}