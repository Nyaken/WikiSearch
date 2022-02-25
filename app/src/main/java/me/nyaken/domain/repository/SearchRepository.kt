package me.nyaken.domain.repository

import me.nyaken.connector.Request

class SearchRepository(
    private val connector: Request
) {
    fun summary() = connector.requestGET("/api/rest_v1/page/summary/Stack_Overflow")
}