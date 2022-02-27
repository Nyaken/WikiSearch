package me.nyaken.connector.method

import android.util.Log
import me.nyaken.connector.Result
import java.io.*
import java.net.HttpURLConnection
import java.net.URLConnection

class PostMultipartMethod(
    conn: HttpURLConnection,
    private val boundary: String
): BaseMethod(conn) {

    fun addFormField(name: String, value: String?) = apply {
        writer.append("--$boundary").append("\r\n")
        writer.append("Content-Disposition: form-data; name=\"$name\"").append("\r\n")
        writer.append("Content-Type: text/plain; charset=UTF-8").append("\r\n")
        writer.append("\r\n")
        writer.append(value).append("\r\n")
        writer.flush()
    }

    fun addFilePart(fieldName: String, uploadFile: File) = apply  {
        val fileName: String = uploadFile.name
        writer.append("--$boundary").append("\r\n")
        writer.append("Content-Disposition: form-data; name=\"$fieldName\"; filename=\"$fileName\"")
        writer.append("\r\n")
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName))
        writer.append("\r\n")
        writer.append("Content-Transfer-Encoding: binary").append("\r\n")
        writer.append("\r\n")
        writer.flush()
        val inputStream = FileInputStream(uploadFile)
        val buffer = ByteArray(4096)
        var bytesRead = -1
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
        outputStream.flush()
        inputStream.close()
        writer.append("\r\n")
        writer.flush()
    }

    override fun build(): Result<String> {
        writer.flush()
        writer.append("--$boundary--").append("\r\n")
        writer.close()

        return super.build()
    }

}