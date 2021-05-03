package com.example.mystockapp.data.remote.api

import com.example.mystockapp.domain.model.CompanyProfile
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

interface CompaniesService {
    @GET("timmyCoder/listing/main/listing.json")
    fun getCompanies(): Observable<List<CompanyProfile>>
}