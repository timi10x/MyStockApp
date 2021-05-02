package com.example.mystockapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockItem(
    val name: String,
    val logo: String = "",
    val ticker: String,
    val latestPrice: Double,
    val previousClose: Double
) : Parcelable