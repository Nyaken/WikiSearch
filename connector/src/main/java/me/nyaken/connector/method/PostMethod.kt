package me.nyaken.connector.method

import me.nyaken.connector.Result
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URLEncoder

class PostMethod(
    conn: HttpURLConnection,
    private val postDataParams: JSONObject
): BaseMethod(conn) {

    fun addParam(name: String, value: String?) = apply {
        postDataParams.put(name, value)
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

    override fun build(): Result<String> {
        writer.write(encodeParams(postDataParams))
        writer.flush()
        writer.close()
        outputStream.close()
        return super.build()
    }
}