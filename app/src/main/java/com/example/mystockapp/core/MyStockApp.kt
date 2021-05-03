package com.example.mystockapp.core

import android.app.Application
import com.example.mystockapp.data.local.StockDatabase
import com.example.mystockapp.data.repository.CompanyRepository

class MyStockApp : Application() {
    private val database by lazy { StockDatabase.getDatabase(this) }
    val repository by lazy { CompanyRepository(database.companyDao()) }
}