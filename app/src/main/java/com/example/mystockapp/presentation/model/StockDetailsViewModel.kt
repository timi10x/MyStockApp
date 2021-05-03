package com.example.mystockapp.presentation.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystockapp.core.di.NetworkModule
import com.example.mystockapp.domain.model.Candle
import com.example.mystockapp.domain.model.StockItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class StockDetailsViewModel : ViewModel() {

    private val candleClient = NetworkModule.getClient()
    private val disposeBag: CompositeDisposable = CompositeDisposable()

    private val _candle = MutableLiveData<Candle>()
    var candle: LiveData<Candle> = _candle

    fun fetchCandles(stockItem: StockItem, resolution: String, from: Long) {
        val candle = candleClient?.retrofitCandleInstance?.getCandles(
            stockItem.ticker,
            resolution,
            from,
            System.currentTimeMillis() / 1000
        )
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                _candle.value = it
            },
                { throwable ->
                    Log.i(Log.getStackTraceString(throwable), throwable.message.toString())
                }
            )

        disposeBag.add(candle!!)
    }
}