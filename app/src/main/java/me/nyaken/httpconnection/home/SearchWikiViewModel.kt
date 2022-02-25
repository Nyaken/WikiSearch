package me.nyaken.httpconnection.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.nyaken.connector.Result
import me.nyaken.domain.repository.SearchRepository
import me.nyaken.network.model.RelatedResponse
import me.nyaken.network.model.SummaryResponse
import me.nyaken.network.model.WikiData
import javax.inject.Inject

@HiltViewModel
class SearchWikiViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {

    private val _query = MutableLiveData<String>()
    val query: LiveData<String>
        get() = _query
    fun query(item: String) {
        _query.value = item.replace(" ", "_").trim()
    }

    private val _summary = MutableLiveData<SummaryResponse>()
    val summary: LiveData<SummaryResponse>
        get() = _summary

    private fun summary(item: SummaryResponse) {
        _summary.postValue(item)
    }

    private val _related = MutableLiveData<List<WikiData>>()
    val related: LiveData<List<WikiData>>
        get() = _related

    private fun related(item: List<WikiData>) {
        _related.postValue(item)
    }

    suspend fun searchData() {
        viewModelScope.launch(Dispatchers.Default) {
            query.value?.let {
                when(val result = searchRepository.summary(it)) {
                    is Result.Success<String> -> {
                        val res: SummaryResponse = Gson().fromJson(result.data, SummaryResponse::class.java)
                        summary(res)
                    }
                    else -> {

                    }
                }
            }
        }
    }

    suspend fun relatedData() {
        viewModelScope.launch(Dispatchers.Default) {
            query.value?.let {
                when(val result = searchRepository.related(it)) {
                    is Result.Success<String> -> {
                        val res: RelatedResponse = Gson().fromJson(result.data, RelatedResponse::class.java)
                        related(res.pages)
                    }
                    else -> {

                    }
                }
            }
        }
    }
}