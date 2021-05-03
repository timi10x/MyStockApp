package com.example.mystockapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mystockapp.data.local.dao.CompanyDao
import com.example.mystockapp.domain.model.CompanyProfile

@Database(
    entities = [CompanyProfile::class],
    version = 1, exportSchema = false
)
abstract class StockDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: StockDatabase? = null

        fun getDatabase(context: Context): StockDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StockDatabase::class.java,
                    "stock_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun companyDao(): CompanyDao
}
