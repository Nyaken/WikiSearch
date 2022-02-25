package me.nyaken.httpconnection.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nyaken.domain.repository.SearchRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {

    suspend fun test() =
        withContext(Dispatchers.Default) {
            searchRepository.summary()
        }
}