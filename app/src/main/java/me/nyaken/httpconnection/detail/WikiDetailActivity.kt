package me.nyaken.httpconnection.detail

import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import dagger.hilt.android.AndroidEntryPoint
import me.nyaken.common.INTENT_DETAIL_URL
import me.nyaken.httpconnection.BaseActivity
import me.nyaken.httpconnection.R
import me.nyaken.httpconnection.databinding.ActivityWebDetailBinding

@AndroidEntryPoint
class WikiDetailActivity: BaseActivity<ActivityWebDetailBinding>(R.layout.activity_web_detail) {

    private val url: String? by lazy {
        intent?.getStringExtra(INTENT_DETAIL_URL)
    }

    override fun viewBinding() = Unit

    override fun setupObserve() = Unit

    override fun onRefresh() = Unit

    override fun initLayout() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.webview.webChromeClient = WebChromeClient()
        binding.webview.webViewClient = WebViewClient()

        url?.let {
            binding.webview.loadUrl(it)
        } ?: finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}