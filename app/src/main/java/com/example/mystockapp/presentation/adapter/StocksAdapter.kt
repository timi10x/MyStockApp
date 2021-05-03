package com.example.android.stockshow.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mystockapp.databinding.ItemStockBinding
import com.example.mystockapp.domain.model.StockItem
import com.squareup.picasso.Picasso
import kotlin.math.abs


class StocksAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<StocksAdapter.StockViewHolder>() {

    private var stockList = mutableListOf<StockItem>()

    private var stockListFull = mutableListOf<StockItem>()

    inner class StockViewHolder(private val binding: ItemStockBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        @Synchronized
        fun bind(stockItem: StockItem, position: Int) {
            binding.run {
                companyNameTv.text = stockItem.name
                tickerTv.text = stockItem.ticker
                currentPriceTv.text = "$%.2f ".format(stockItem.latestPrice)
                if (stockItem.logo.isNotEmpty())
                    Picasso.get()
                        .load(stockItem.logo)
                        .into(logoIv)
                val stockDelta = stockItem.latestPrice - stockItem.previousClose
                val stockRisePercent = abs(stockDelta) / stockItem.previousClose * 100
                if (stockDelta < 0) {
                    dayDeltaTv.setTextColor(Color.RED)
                    dayDeltaTv.text = "-$%.2f (%.2f".format(-stockDelta, stockRisePercent) + "%)"
                } else {
                    dayDeltaTv.text = "+$%.2f (%.2f".format(stockDelta, stockRisePercent) + "%)"
                    dayDeltaTv.setTextColor(Color.GREEN)
                }

                binding.root.setOnClickListener {
                    listener.onItemClick(stockItem)
                }
            }
        }
    }

    fun submitList(stockList: List<StockItem>) {
        this.stockList = stockList as MutableList<StockItem>
        this.stockListFull = ArrayList(this.stockList)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(stockItem: StockItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = stockList[position]
        holder.bind(stock, position)
    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    companion object DiffCallback : DiffUtil.ItemCallback<StockItem>() {
        override fun areItemsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem.ticker == newItem.ticker
        }
    }
}