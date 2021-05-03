package com.example.mystockapp.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.stockshow.ui.adapters.StocksAdapter
import com.example.mystockapp.R
import com.example.mystockapp.core.MyStockApp
import com.example.mystockapp.databinding.FragmentHomeBinding
import com.example.mystockapp.domain.model.StockItem
import com.example.mystockapp.domain.vmfactory.StocksViewModelFactory
import com.example.mystockapp.presentation.adapter.SearchAdapter
import com.example.mystockapp.presentation.model.SharedViewModel
import com.example.mystockapp.presentation.model.StocksViewModel
import com.example.mystockapp.presentation.utils.viewBinding

class HomeFragment : Fragment(), SearchAdapter.OnItemClickListener, StocksAdapter.OnItemClickListener  {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var searchAdapter: SearchAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var mSearchView: SearchView
    private lateinit var stockViewModel: StocksViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = ""
        (activity as MainActivity).supportActionBar?.subtitle = ""

        searchAdapter = SearchAdapter(this)
        binding.searchRv.adapter = searchAdapter
        binding.stocksRv.adapter = StocksAdapter(this)
        binding.stocksRv.setHasFixedSize(false)

        val adapter = binding.stocksRv.adapter as StocksAdapter


        sharedViewModel.searchMap.observe(viewLifecycleOwner) {
            searchAdapter.submitList(it.values.toList())
        }

        setHasOptionsMenu(true)

        initVm()

        stockViewModel.stockMap.observe(viewLifecycleOwner) { map ->
            binding.progressBar.visibility = View.GONE
            sharedViewModel.setStockMap(map)
        }

        sharedViewModel.stockMap.observe(viewLifecycleOwner) {
            val stockList = it.values.toList()
            adapter.submitList(stockList)
            adapter.notifyDataSetChanged()
        }

        stockViewModel.setupStockMap()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    private fun initVm() {
        val app = requireActivity().application as MyStockApp
        val repo = app.repository
        val factory = StocksViewModelFactory(repo)
        stockViewModel = ViewModelProvider(this, factory).get(StocksViewModel::class.java)

    }

    override fun onItemClick(stockItem: StockItem) {
        val action = HomeFragmentDirections.actionHomeFragmentToStockDetailsFragment(stockItem)
        findNavController().navigate(action)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        val searchMenuItem = menu.findItem(R.id.menu_search)

        mSearchView = searchMenuItem.actionView as SearchView

        mSearchView.onActionViewExpanded()

        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                if (sharedViewModel.stockMap.value != null) {
                    sharedViewModel.setSearchMap(sharedViewModel.stockMap.value!!)
                }

                binding.searchRv.visibility =
                    View.VISIBLE

                mSearchView.requestFocus()

                val imm =
                    activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(mSearchView.findFocus(), 0)

                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
               binding.searchRv.visibility = View.GONE

                val imm =
                    activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)

                return true
            }
        })

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchAdapter.filter.filter(newText)

                binding.searchRv.visibility = View.GONE

                binding.searchRv.visibility = View.VISIBLE

                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = ""
        (activity as MainActivity).supportActionBar?.subtitle = ""
    }
}