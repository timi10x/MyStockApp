package com.example.mystockapp.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mystockapp.R
import com.example.mystockapp.databinding.FragmentStockDetailsBinding
import com.example.mystockapp.presentation.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockDetailsFragment : Fragment(R.layout.fragment_stock_details) {

    private val binding by viewBinding(FragmentStockDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
      @JvmStatic
        fun newInstance() = StockDetailsFragment()
    }
}