package me.nyaken.connector

import me.nyaken.connector.method.GetMethod
import me.nyaken.connector.method.PostMethod
import me.nyaken.connector.method.PostMultipartMethod
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.HashMap

class Request(
    private val url: String?,
    private val headers: MutableMap<String, String>?,
    private val readTimeout: Int?,
    private val connectTimeout: Int?
) {

    private val DEFAULT_TIMEOUT = 3000

    fun requestGET(
        path: String
    ): GetMethod {
        val conn: HttpURLConnection = httpConnection(URL("$url$path"))
        conn.requestMethod = "GET"

        return GetMethod(conn)
    }

    fun requestPOST(
        path: String,
        params: JSONObject? = null
    ): PostMethod {
        val conn: HttpURLConnection = httpConnection(URL("$url$path"))
        conn.requestMethod = "POST"

        return PostMethod(conn, params ?: JSONObject())
    }

    fun requestPUT(
        path: String,
        params: JSONObject? = null
    ): PostMethod {
        val conn: HttpURLConnection = httpConnection(URL("$url$path"))
        conn.requestMethod = "PUT"

        return PostMethod(conn, params ?: JSONObject())
    }

    fun requestDELETE(
        path: String,
        params: JSONObject? = null
    ): PostMethod {
        val conn: HttpURLConnection = httpConnection(URL("$url$path"))
        conn.requestMethod = "DELETE"

        return PostMethod(conn, params ?: JSONObject())
    }

    fun requestMultiPart(
        path: String
    ): PostMultipartMethod {
        val boundary: String = UUID.randomUUID().toString()
        val conn: HttpURLConnection = httpConnection(URL("$url$path"))
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")

        return PostMultipartMethod(conn, boundary)
    }

    private fun httpConnection(
        url: URL
    ): HttpURLConnection {
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.readTimeout = readTimeout ?: DEFAULT_TIMEOUT
        conn.connectTimeout = connectTimeout ?: DEFAULT_TIMEOUT

        headers?.let { map ->
            map.forEach {
                conn.setRequestProperty(it.key, it.value)
            }
        }
        return conn
    }

    open class Builder {
        private var url: String? = null
        private var headers: MutableMap<String, String> = HashMap()
        private var readTimeOut: Int? = null
        private var connectTimeout: Int? = null

        open fun addHeader(name: String, value: String): Builder = apply {
            this.headers[name] = value
        }

        open fun url(url: String): Builder = apply {
            this.url = url
        }

        open fun readTimeOut(timeout: Int) = apply {
            this.readTimeOut = timeout
        }

        open fun connectTimeout(timeout: Int) = apply {
            this.connectTimeout = timeout
        }

        open fun build(): Request {
            return Request(
                url,
                headers,
                readTimeOut ,
                connectTimeout
            )
        }
    }
}