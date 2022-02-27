package me.nayken.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.nayken.sample.databinding.ActivityMainBinding
import me.nyaken.connector.Request
import me.nyaken.connector.Result
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class MainActivity : AppCompatActivity() {
    private val testBasedUrl = "http://postman-echo.com/"

    private lateinit var binding: ActivityMainBinding

    private val request: Request by lazy {
        Request.Builder()
            .url(testBasedUrl)
            .connectTimeout(10000)
            .readTimeOut(10000)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonGet.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                when(val response = requestGet()) {
                    is Result.Success<String> -> {
                        attachResponse(response.data)
                    }
                    else -> {}
                }
            }
        }

        binding.buttonPost.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                when(val response = requestPost()) {
                    is Result.Success<String> -> {
                        attachResponse(response.data)
                    }
                    else -> {}
                }
            }
        }

        binding.buttonMultipartPost.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                when(val response = requestPostMultipart()) {
                    is Result.Success<String> -> {
                        attachResponse(response.data)
                    }
                    else -> {}
                }
            }
        }

        binding.buttonPut.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                when(val response = requestPut()) {
                    is Result.Success<String> -> {
                        attachResponse(response.data)
                    }
                    else -> {}
                }
            }
        }

        binding.buttonDelete.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                when(val response = requestDELETE()) {
                    is Result.Success<String> -> {
                        attachResponse(response.data)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun attachResponse(
        response: String?
    ) {
        lifecycleScope.launch(Dispatchers.Main) {
            val gson = GsonBuilder().setPrettyPrinting().create()
            binding.textviewLog.text =
                gson.toJson(JsonParser.parseString(response))
        }
    }

    private suspend fun requestGet(): Result<String> {
        return withContext(Dispatchers.IO) {
            request.requestGET(path = "get?foo1=bar1&foo2=bar2").build()
        }
    }

    private suspend fun requestPost(): Result<String> {
        val params: JSONObject = JSONObject()
        params.put("key1", "value1")
        params.put("key2", "value2")

        return withContext(Dispatchers.IO) {
            request.requestPOST(
                path = "post",
                params = params
            )
                .build()
        }
    }

    private suspend fun requestPut(): Result<String> {
        return withContext(Dispatchers.IO) {
            request.requestPUT(
                path = "put"
            )
                .addParam("key1", "value1")
                .addParam("key2", "value2")
                .build()
        }
    }

    private suspend fun requestDELETE(): Result<String> {
        return withContext(Dispatchers.IO) {
            request.requestDELETE(path = "delete")
                .addParam(name = "key1", value = "value1")
                .addParam(name = "key2", value = "value2")
        }
            .build()
    }

    private suspend fun requestPostMultipart(): Result<String> {
        val filePath = filesDir.path + "/myText.txt"
        writeTextToFile(filePath)
        return withContext(Dispatchers.IO) {
            request.requestMultiPart(path = "post")
                .addFormField(name = "key1", value = "value1")
                .addFormField(name = "key2", value = "value2")
                .addFilePart(name ="file1", uploadFile = File(filePath))
                .build()
        }
    }

    private fun writeTextToFile(path: String) {
        val file = File(path)
        val fileWriter = FileWriter(file, false)
        val bufferedWriter = BufferedWriter(fileWriter)

        bufferedWriter.append("Test1\n")
        bufferedWriter.append("Test2")
        bufferedWriter.newLine()
        bufferedWriter.append("Test3\n")
        bufferedWriter.close()
    }
}