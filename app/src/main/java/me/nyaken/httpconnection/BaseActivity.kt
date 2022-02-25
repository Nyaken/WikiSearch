package me.nyaken.httpconnection

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<DataBinding : ViewDataBinding>(
    @LayoutRes val layoutId: Int
): AppCompatActivity() {

    protected lateinit var binding: DataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<DataBinding>(this, layoutId).also { binding = it }

        onNewIntent(intent)
        viewBinding()
        setupObserve()
        initLayout()

    }

    protected abstract fun viewBinding()

    protected abstract fun setupObserve()

    protected abstract fun initLayout()

    protected abstract fun onRefresh()
}