package me.nyaken.httpconnection.home

import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.nyaken.connector.Result
import me.nyaken.httpconnection.BaseActivity
import me.nyaken.httpconnection.R
import me.nyaken.httpconnection.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModels()

    override fun viewBinding() {
    }

    override fun setupObserve() {
    }

    override fun initLayout() {
        lifecycleScope.launch {
            when(val response = viewModel.test()) {
                is Result.Success<String> -> {
                    Log.d("TEST", response.toString())
                }
                else -> {}
            }
        }
    }

    override fun onRefresh() {

    }
}