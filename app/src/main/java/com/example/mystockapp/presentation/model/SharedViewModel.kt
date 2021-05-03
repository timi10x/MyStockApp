package com.example.mystockapp.presentation.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystockapp.domain.model.StockItem
import java.util.*

class SharedViewModel : ViewModel() {

    private val _stockMap = MutableLiveData<TreeMap<String, StockItem>>()
    var stockMap: LiveData<TreeMap<String, StockItem>> = _stockMap

    private val _searchMap = MutableLiveData<TreeMap<String, StockItem>>()
    val searchMap = _searchMap as LiveData<TreeMap<String, StockItem>>

    fun setStockMap(map: TreeMap<String, StockItem>) {
        _stockMap.value = map
    }

    fun setSearchMap(map: TreeMap<String, StockItem>) {
        _searchMap.value = map
    }


}