package com.example.mystockapp.presentation.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.mystockapp.R
import com.example.mystockapp.databinding.FragmentStockDetailsBinding
import com.example.mystockapp.presentation.model.SharedViewModel
import com.example.mystockapp.presentation.model.StockDetailsViewModel
import com.example.mystockapp.presentation.utils.viewBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.lang.Math.abs

class StockDetailsFragment : Fragment(R.layout.fragment_stock_details) {

    private val binding by viewBinding(FragmentStockDetailsBinding::bind)
    private val args: StockDetailsFragmentArgs by navArgs()

    private lateinit var stockDetailsViewModel: StockDetailsViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        stockDetailsViewModel = ViewModelProvider(this).get(StockDetailsViewModel::class.java)

        val stockItem = args.stockModel

        (activity as MainActivity).supportActionBar?.title = stockItem.ticker
        (activity as MainActivity).supportActionBar?.subtitle = stockItem.name

        stockDetailsViewModel.fetchCandles(stockItem, "W", 0)


        binding.apply {
            detailCurrentPriceTv.text = "$%.2f".format(stockItem.latestPrice)

            //calculate day delta and percent rise
            val stockDelta = stockItem.latestPrice - stockItem.previousClose
            val stockRisePercent = kotlin.math.abs(stockDelta) / stockItem.previousClose * 100
            if (stockDelta < 0) {
                detailDayDeltaTv.setTextColor(Color.RED)
                detailDayDeltaTv.text = "-$%.2f (%.2f".format(-stockDelta, stockRisePercent) + "%)"
            } else {
                detailDayDeltaTv.text = "+$%.2f (%.2f".format(stockDelta, stockRisePercent) + "%)"
                detailDayDeltaTv.setTextColor(Color.GREEN)
            }
        }
        stockDetailsViewModel.candle.observe(viewLifecycleOwner) { candle ->
            val entries = arrayListOf<Entry>()
            val closePriceList = candle.c
            var x = 0
            for (closePrice in closePriceList) {
                entries.add(Entry(x.toFloat(), closePrice.toFloat()))
                x += 10
            }
            val dataSet = LineDataSet(entries, stockItem.ticker)
            dataSet.setDrawCircles(false)
            dataSet.setDrawValues(false)
            dataSet.color = Color.BLACK
            dataSet.lineWidth = 3f

            dataSet.setDrawFilled(true)
            dataSet.fillDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.chart_bg)

            val lineData = LineData(dataSet)

            val lineChart = binding.lineChart

            //setting data
            lineChart.data = lineData
            lineChart.invalidate()

            //customizing
            lineChart.legend.isEnabled = false
            lineChart.setNoDataText("")
            lineChart.description.isEnabled = false
            lineChart.xAxis.isEnabled = false
            lineChart.axisLeft.isEnabled = false
            lineChart.axisRight.isEnabled = false
        }
    }

    companion object {
      @JvmStatic
        fun newInstance() = StockDetailsFragment()
    }
}