package me.nayken.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.nayken.sample.databinding.ActivityMainBinding
import me.nyaken.connector.Request
import me.nyaken.connector.Result
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val request: Request by lazy {
        Request.Builder()
            .url("https://webhook.site/caeaf4a0-3407-4b8e-b7ab-277bc1a65bbe/")
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
                        attachLog("GET", response.toString())
                    }
                    else -> {}
                }
            }
        }

        binding.buttonPost.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                when(val response = requestPost()) {
                    is Result.Success<String> -> {
                        attachLog("POST", response.toString())
                    }
                    else -> {}
                }
            }
        }

        binding.buttonMultipartPost.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                when(val response = requestPostMultipart()) {
                    is Result.Success<String> -> {
                        attachLog("MULTIPART POST", response.toString())
                    }
                    else -> {}
                }
            }
        }

        binding.buttonPut.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                when(val response = requestPut()) {
                    is Result.Success<String> -> {
                        attachLog("PUT", response.toString())
                    }
                    else -> {}
                }
            }
        }

        binding.buttonDelete.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                when(val response = requestDELETE()) {
                    is Result.Success<String> -> {
                        attachLog("DELETE", response.toString())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun attachLog(
        type: String,
        log: String?
    ) {
        binding.textviewLog.text = "$type $log"
    }

    private suspend fun requestGet(): Result<String> {
        return withContext(Dispatchers.IO) {
            request.requestGET(path = "get/test").build()
        }
    }

    private suspend fun requestPost(): Result<String> {
        val params: JSONObject = JSONObject()
        params.put("key1", "value1")
        params.put("key2", "value2")

        return withContext(Dispatchers.IO) {
            request.requestPOST(
                path = "post/test",
                postDataParams = params
            )
                .build()
        }
    }

    private suspend fun requestPut(): Result<String> {
        return withContext(Dispatchers.IO) {
            request.requestPUT(
                path = "put/test"
            )
                .addParam("key1", "value1")
                .addParam("key2", "value2")
                .build()
        }
    }

    private suspend fun requestDELETE(): Result<String> {
        return withContext(Dispatchers.IO) {
            request.requestDELETE(path = "delete/test")
        }
            .build()
    }

    private suspend fun requestPostMultipart(): Result<String> {
        return withContext(Dispatchers.IO) {
            request.requestMultiPart(path = "multipart/test")
                .addFormField(name = "key1", value = "value1")
                .addFormField(name = "key2", value = "value2")
                .build()
        }
    }
}