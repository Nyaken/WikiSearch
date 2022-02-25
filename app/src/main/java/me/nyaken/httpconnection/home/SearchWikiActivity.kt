package me.nyaken.httpconnection.home

import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.nyaken.httpconnection.BaseActivity
import me.nyaken.httpconnection.R
import me.nyaken.httpconnection.databinding.ActivitySearchWikiBinding
import me.nyaken.httpconnection.databinding.ItemWikiHeaderBinding
import me.nyaken.httpconnection.home.adapter.SearchWikiAdapter
import me.nyaken.network.model.SummaryResponse
import me.nyaken.network.model.WikiData

@AndroidEntryPoint
class SearchWikiActivity: BaseActivity<ActivitySearchWikiBinding>(R.layout.activity_search_wiki) {

    private val viewModel: SearchWikiViewModel by viewModels()

    private val adapter: SearchWikiAdapter = SearchWikiAdapter()

    override fun viewBinding() {
        binding.lifecycleOwner = this
    }

    override fun setupObserve() {
        viewModel.summary.observe(this, Observer {
            initSummaryData(it)

            lifecycleScope.launch {
                viewModel.relatedData("Google")
            }
        })

        viewModel.related.observe(this, Observer {
            adapter.setItems(it)
            binding.swipeRefresh.isRefreshing = false
        })
    }

    override fun initLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            onRefresh()
        }

        binding.list.adapter = adapter
        binding.list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if(position == 0) {
                Toast.makeText(this, "header", Toast.LENGTH_SHORT).show()
            } else {
                val item: WikiData = adapter.getItem(position - 1)
                Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
            }
        }

        getData("Google")
    }

    override fun onRefresh() {
        adapter.clearItems()
        getData("Stack")
    }

    private fun getData(query: String) {
        lifecycleScope.launch {
            viewModel.searchData(query)
        }
    }

    private fun initSummaryData(item: SummaryResponse) {
        if(binding.list.headerViewsCount > 0) {
            val oldHeader: View = binding.list.findViewWithTag(this.javaClass.simpleName + "header")
            binding.list.removeHeaderView(oldHeader)
        }
        val bindingSummary = ItemWikiHeaderBinding.inflate(LayoutInflater.from(this), null, false)
        bindingSummary.item = item
        bindingSummary.root.tag = this.javaClass.simpleName + "header"
        binding.list.addHeaderView(bindingSummary.root)
    }
}