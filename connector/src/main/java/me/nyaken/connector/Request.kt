package me.nyaken.connector

import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

class Request(
    private val url: String?,
    private val headers: MutableMap<String, String>?,
    private val readTimeout: Int?,
    private val connectTimeout: Int?
) {

    private val DEFAULT_TIMEOUT = 3000

    fun requestGET(
        path: String
    ): Result<String> {
        val conn: HttpURLConnection = URL("$url$path").openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.readTimeout = readTimeout ?: DEFAULT_TIMEOUT
        conn.connectTimeout = connectTimeout ?: DEFAULT_TIMEOUT
        conn.doInput = true
        conn.doOutput = false

        headers?.let { map ->
            map.forEach {
                conn.setRequestProperty(it.key, it.value)
            }
        }

        return try {
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val bufferReaderIn = BufferedReader(InputStreamReader(conn.inputStream))
                var inputLine: String?
                val response = StringBuffer()
                while (bufferReaderIn.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                bufferReaderIn.close()
                Result.Success(response.toString())
            } else {
                Result.Error(Exception("Cannot open HttpURLConnection"))
            }
        } catch (e: SocketTimeoutException) {
            Result.Error(Exception(e.message))
        }
    }

    fun requestPOST(
        path: String,
        postDataParams: JSONObject
    ): Result<String> {
        val conn: HttpURLConnection = URL("$url$path").openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.readTimeout = readTimeout ?: DEFAULT_TIMEOUT
        conn.connectTimeout = connectTimeout ?: DEFAULT_TIMEOUT
        conn.doInput = true
        conn.doOutput = true

        headers?.let { map ->
            map.forEach {
                conn.setRequestProperty(it.key, it.value)
            }
        }

        val os: OutputStream = conn.outputStream
        val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
        writer.write(encodeParams(postDataParams))
        writer.flush()
        writer.close()
        os.close()

        return try {
            if (conn.responseCode == HttpsURLConnection.HTTP_OK) {
                val bufferReaderIn = BufferedReader(InputStreamReader(conn.inputStream))
                var inputLine: String?
                val response = StringBuffer()
                while (bufferReaderIn.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                bufferReaderIn.close()
                Result.Success(response.toString())
            } else {
                Result.Error(Exception("Cannot open HttpURLConnection"))
            }
        } catch (e: SocketTimeoutException) {
            Result.Error(Exception(e.message))
        }
    }

    fun requestPUT(
        path: String,
        postDataParams: JSONObject
    ): Result<String> {
        val conn: HttpURLConnection = URL("$url$path").openConnection() as HttpURLConnection
        conn.requestMethod = "PUT"
        conn.readTimeout = readTimeout ?: DEFAULT_TIMEOUT
        conn.connectTimeout = connectTimeout ?: DEFAULT_TIMEOUT
        conn.doInput = true
        conn.doOutput = false

        headers?.let { map ->
            map.forEach {
                conn.setRequestProperty(it.key, it.value)
            }
        }

        val os: OutputStream = conn.outputStream
        val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
        writer.write(encodeParams(postDataParams))
        writer.flush()
        writer.close()
        os.close()

        return try {
            if (conn.responseCode == HttpsURLConnection.HTTP_OK) {
                val bufferReaderIn = BufferedReader(InputStreamReader(conn.inputStream))
                var inputLine: String?
                val response = StringBuffer()
                while (bufferReaderIn.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                bufferReaderIn.close()
                Result.Success(response.toString())
            } else {
                Result.Error(Exception("Cannot open HttpURLConnection"))
            }
        } catch (e: SocketTimeoutException) {
            Result.Error(Exception(e.message))
        }
    }

    fun requestDELETE(
        path: String
    ): Result<String> {
        val conn: HttpURLConnection = URL("$url$path").openConnection() as HttpURLConnection
        conn.requestMethod = "DELETE"
        conn.readTimeout = readTimeout ?: DEFAULT_TIMEOUT
        conn.connectTimeout = connectTimeout ?: DEFAULT_TIMEOUT
        conn.doInput = true
        conn.doOutput = false

        headers?.let { map ->
            map.forEach {
                conn.setRequestProperty(it.key, it.value)
            }
        }

        return try {
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val bufferReaderIn = BufferedReader(InputStreamReader(conn.inputStream))
                var inputLine: String?
                val response = StringBuffer()
                while (bufferReaderIn.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                bufferReaderIn.close()
                Result.Success(response.toString())
            } else {
                Result.Error(Exception("Cannot open HttpURLConnection"))
            }
        } catch (e: SocketTimeoutException) {
            Result.Error(Exception(e.message))
        }
    }

    @Throws(IOException::class)
    private fun encodeParams(params: JSONObject): String {
        val result = StringBuilder()
        var first = true
        val itr = params.keys()
        while (itr.hasNext()) {
            val key = itr.next()
            val value = params[key]
            if (first) first = false else result.append("&")
            result.append(URLEncoder.encode(key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }
        return result.toString()
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