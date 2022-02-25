package me.nyaken.httpconnection.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import me.nyaken.httpconnection.databinding.ItemWikiBinding
import me.nyaken.network.model.WikiData

class SearchWikiAdapter() : BaseAdapter() {

    private val items: ArrayList<WikiData> = ArrayList()

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): WikiData = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {

        val binding = ItemWikiBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        binding.item = items[position]

        return binding.root
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    fun setItems(item: List<WikiData>) {
        items.addAll(item)
        notifyDataSetChanged()
    }
}