package com.example.mystockapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mystockapp.domain.model.CompanyProfile

@Dao
interface CompanyDao {
    @Query("SELECT * FROM company_table")
    fun getCompanies(): List<CompanyProfile>

    @Insert
    fun insert(companyProfile: CompanyProfile)
}
