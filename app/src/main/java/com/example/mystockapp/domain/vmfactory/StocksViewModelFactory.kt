package com.example.mystockapp.domain.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystockapp.data.repository.CompanyRepository
import com.example.mystockapp.presentation.model.StocksViewModel

class StocksViewModelFactory(
    private val repository: CompanyRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StocksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StocksViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}