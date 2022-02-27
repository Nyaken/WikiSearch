package me.nyaken.httpconnection.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.nyaken.common.INTENT_QUERY
import me.nyaken.httpconnection.BaseActivity
import me.nyaken.httpconnection.R
import me.nyaken.httpconnection.databinding.ActivitySearchWikiBinding
import me.nyaken.httpconnection.databinding.ItemWikiHeaderBinding
import me.nyaken.httpconnection.detail.WikiDetailActivity
import me.nyaken.httpconnection.home.adapter.SearchWikiAdapter
import me.nyaken.network.model.SummaryResponse
import me.nyaken.network.model.WikiData

@AndroidEntryPoint
class SearchWikiActivity: BaseActivity<ActivitySearchWikiBinding>(R.layout.activity_search_wiki) {

    private val viewModel: SearchWikiViewModel by viewModels()

    private val adapter: SearchWikiAdapter = SearchWikiAdapter()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.getStringExtra(INTENT_QUERY)?.let {
            viewModel.query(it)
        }
    }

    override fun viewBinding() {
        binding.lifecycleOwner = this
    }

    override fun setupObserve() {
        viewModel.query.observe(this, Observer {
            supportActionBar?.title = it
        })

        viewModel.summary.observe(this, Observer {
            initSummaryData(it)

            lifecycleScope.launch {
                viewModel.relatedData()
            }
        })

        viewModel.related.observe(this, Observer {
            adapter.setItems(it)
            binding.swipeRefresh.isRefreshing = false
        })
    }

    override fun initLayout() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.swipeRefresh.setOnRefreshListener {
            onRefresh()
        }

        binding.list.emptyView = binding.textviewEmpty
        binding.list.adapter = adapter
        binding.list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if(position == 0) {
                startActivity(
                    Intent(
                        this,
                        WikiDetailActivity::class.java
                    ).apply {
                        putExtra(INTENT_QUERY, viewModel.query.value)
                    }
                )
            } else {
                val item: WikiData = adapter.getItem(position - 1)
                startActivity(
                    Intent(
                    this,
                    SearchWikiActivity::class.java
                    ).apply {
                        putExtra(INTENT_QUERY, item.title)
                    }
                )
            }
        }

        getData()
    }

    override fun onRefresh() {
        adapter.clearItems()
        getData()
    }

    private fun getData() {
        lifecycleScope.launch {
            viewModel.searchData()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            maxWidth = Integer.MAX_VALUE

            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.query(it)
                        onRefresh()
                    }
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean = false

            })
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}