package com.neo.ilapagerassignment.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.neo.ilapagerassignment.R
import com.neo.ilapagerassignment.adapter.GadgetsPagerAdapter
import com.neo.ilapagerassignment.adapter.SubItemsListAdapter
import com.neo.ilapagerassignment.core.ApiState
import com.neo.ilapagerassignment.databinding.ActivityMainBinding
import com.neo.ilapagerassignment.model.Gadgets
import com.neo.ilapagerassignment.model.SubItems
import com.neo.ilapagerassignment.utils.setCarouselEffects
import com.neo.ilapagerassignment.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var pagerAdapter: GadgetsPagerAdapter
    private lateinit var subItemsAdapter: SubItemsListAdapter

    private var listGadgets = arrayListOf<Gadgets>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        initPagerView()
        initViewPagerListener()
        initModelsView()
        initObserver()

        mainViewModel.getGadgetsList()
    }

    private fun initPagerView() {
        pagerAdapter = GadgetsPagerAdapter()
        binding.pagerGadgets.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = pagerAdapter
            TabLayoutMediator(
                binding.tabIndicator,
                binding.pagerGadgets
            ) { _, _ ->
            }.attach()
            setCarouselEffects()
        }
    }

    private fun initViewPagerListener() {
        binding.pagerGadgets.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bindModelsData(listGadgets[position].listItems)
            }
        })
    }

    private fun initModelsView() {
        subItemsAdapter = SubItemsListAdapter()
        binding.recyclerModels.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = subItemsAdapter
            isNestedScrollingEnabled = true
            addItemDecoration(DividerItemDecoration(
                binding.recyclerModels.context,
                LinearLayout.VERTICAL
            ))
        }
    }

    private fun bindGadgetsData(listItems: List<Gadgets>) {
        listGadgets = listItems as ArrayList<Gadgets>
        pagerAdapter.setItems(listGadgets)
        bindModelsData(listGadgets[0].listItems)
    }

    private fun bindModelsData(listItems: List<SubItems>) {
        subItemsAdapter.setItems(listItems)
        setFilter(listItems)
        resetFilter()
    }

    private fun setFilter(listItems: List<SubItems>) {

        val allItems = arrayListOf<SubItems>()

        binding.edtSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                allItems.clear()
                if (listItems.isNotEmpty()) {
                    val data = listItems.filter { item: SubItems ->
                        item.itemName.lowercase().contains(query.lowercase())
                    }
                    allItems.addAll(data)
                }
                bindDataToView(allItems)
                return false
            }

        })

        //Display close button on search bar
        val closeButtonId = resources.getIdentifier("android:id/search_close_btn", null, null)
        val closeButtonImage = binding.edtSearchView.findViewById(closeButtonId) as ImageView
        closeButtonImage.setImageResource(R.drawable.ic_close)

        closeButtonImage.setOnClickListener {
            resetFilter()
            closeButtonImage.visibility = View.GONE
            subItemsAdapter.setItems(listItems)
        }
    }

    fun bindDataToView(listItems: List<SubItems>) {
        if (listItems.isEmpty()) {
            binding.txtNoDataAvailable.visibility = View.VISIBLE
            binding.recyclerModels.visibility = View.GONE
        } else {
            binding.txtNoDataAvailable.visibility = View.GONE
            binding.recyclerModels.visibility = View.VISIBLE
            subItemsAdapter.setItems(listItems)
        }
    }

    private fun resetFilter() {
        binding.txtNoDataAvailable.visibility = View.GONE
        binding.recyclerModels.visibility = View.VISIBLE
        binding.edtSearchView.setQuery("", true)
        binding.edtSearchView.clearFocus()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            mainViewModel._gadgetsDataListFlow.collect {
                when (it) {
                    is ApiState.Loading -> {
                        Toast.makeText(this@MainActivity, "Loading Master Data", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Failure :: " + it.errMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is ApiState.Success -> {
                        Toast.makeText(this@MainActivity, "Data Retrieved", Toast.LENGTH_SHORT)
                            .show()
                        bindGadgetsData(it.T as ArrayList<Gadgets>)
                    }
                    is ApiState.Empty -> {

                    }
                }
            }
        }
    }
}