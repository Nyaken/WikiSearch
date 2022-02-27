package me.nyaken.connector.method

import me.nyaken.connector.Result
import java.io.*
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

abstract class BaseMethod(
    private val conn: HttpURLConnection
) {
    protected lateinit var outputStream: OutputStream
    protected lateinit var writer: BufferedWriter

    init {
        when(conn.requestMethod) {
            "GET" -> {
                conn.doInput = true
                conn.doOutput = false
            } else -> {
                conn.doInput = true
                conn.doOutput = true
                outputStream = conn.outputStream
                writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
            }
        }
    }

    open fun build(): Result<String> {
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
}