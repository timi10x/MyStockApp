package com.example.mystockapp.presentation.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystockapp.core.di.NetworkModule
import com.example.mystockapp.data.repository.CompanyRepository
import com.example.mystockapp.domain.model.CompanyProfile
import com.example.mystockapp.domain.model.StockItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class StocksViewModel(private val repo: CompanyRepository) : ViewModel() {

    //clients
    private val companiesClient = NetworkModule.getClient()
    private val quoteClient = NetworkModule.getClient()

    private val disposeBag: CompositeDisposable = CompositeDisposable()


    //storage
    var stockMap: MutableLiveData<TreeMap<String, StockItem>> =
        MutableLiveData<TreeMap<String, StockItem>>()
    private var companyList: List<CompanyProfile>? = repo.companyList

    private suspend fun fetchCompanies() = withContext(Dispatchers.IO) {
        val disposable = companiesClient?.retrofitCompaniesInstance?.getCompanies()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ companies ->
                companyList = companies
                viewModelScope.launch {
                    createStocks()
                }

                insertCompaniesToDB()
            },
                { throwable ->
                    Log.i(
                        Log.getStackTraceString(throwable),
                        throwable.message.toString()
                    )
                }
            )

        disposeBag.add(disposable!!)
    }

    private fun insertCompaniesToDB() {
        for (company in companyList!!) {
            repo.insert(company)
        }
    }

    private suspend fun createStocks() = withContext(Dispatchers.IO) {
        val map = TreeMap<String, StockItem>()

        for (company in companyList!!) {
            val disposable = quoteClient?.retrofitQuoteInstance?.getQuote(company.ticker)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ quote ->
                    val stockItem = StockItem(
                        company.name,
                        company.logo,
                        company.ticker,
                        quote.latestPrice,
                        quote.previousClose
                    )

                    map[company.ticker] = stockItem
                    stockMap.value = map
                },
                    { throwable ->
                        Log.i(
                            Log.getStackTraceString(throwable),
                            throwable.message.toString()
                        )
                    })

            disposeBag.add(disposable!!)

            delay(10)
        }
    }



    override fun onCleared() {
        super.onCleared()
        disposeBag.clear()
    }

    fun setupStockMap() {
        if (companyList.isNullOrEmpty()) {
            viewModelScope.launch {
                fetchCompanies()
            }
        } else {
            viewModelScope.launch {
                createStocks()
            }
        }
    }
}