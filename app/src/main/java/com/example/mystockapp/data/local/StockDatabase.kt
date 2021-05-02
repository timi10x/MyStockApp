package com.example.mystockapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mystockapp.data.local.dao.CompanyDao
import com.example.mystockapp.domain.model.CompanyProfile

@Database(
    entities = [CompanyProfile::class],
    version = 1, exportSchema = false
)
abstract class StockDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "stock_database"
    }

    abstract fun companyDao(): CompanyDao
}
