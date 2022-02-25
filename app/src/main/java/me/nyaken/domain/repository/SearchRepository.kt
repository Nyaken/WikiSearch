package me.nyaken.domain.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nyaken.connector.Request
import me.nyaken.connector.Result

class SearchRepository(
    private val connector: Request
) {
    suspend fun summary(query: String): Result<String> {
        return withContext(Dispatchers.IO) {
            connector.requestGET("/api/rest_v1/page/summary/$query")
        }
    }

    suspend fun related(query: String): Result<String> {
        return withContext(Dispatchers.IO) {
            connector.requestGET("/api/rest_v1/page/related/$query")
        }
    }
}

/**
 * 검색 상세 페이지: https://en.wikipedia.org/api/rest_v1/page/html/{검색어}
- 요약 정보 API: https://en.wikipedia.org/api/rest_v1/page/summary/{검색어}
- 연관 검색 API: https://en.wikipedia.org/api/rest_v1/page/related/{검색어}**/