package com.moradyar.networkcore.impl

import com.moradyar.networkcore.core.HttpClient
import com.moradyar.networkcore.core.HttpException
import com.moradyar.networkcore.core.HttpRequest
import com.moradyar.networkcore.core.RequestState
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

internal class DefaultHttpClient(
    private val baseUrl: String
) : HttpClient {

    private val client = OkHttpClient
        .Builder()
        .build()

    private val executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE)

    override fun fetch(
        httpRequest: HttpRequest,
        onResponseReady: (response: RequestState) -> Unit
    ) {
        client
            .newCall(createRequestBuilder(httpRequest))
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    onResponseReady(RequestState.Error(e))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        onResponseReady(RequestState.Success(response.body?.string() ?: ""))
                    } else {
                        onResponseReady(
                            RequestState.Error(
                                HttpException(
                                    response.code,
                                    response.body?.string() ?: ""
                                )
                            )
                        )
                    }
                }
            })
    }

    override fun fetchRaw(
        httpRequest: HttpRequest,
        onResponseReady: (response: RequestState) -> Unit
    ) {
        executorService.execute {
            try {
                val response = fetchRawData(httpRequest)
                onResponseReady(RequestState.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                onResponseReady(RequestState.Error(e))
            }
        }
    }

    private fun fetchRawData(httpRequest: HttpRequest): String {
        val parameters = httpRequest.getQueryParametersString()
        val baseUrl = "$baseUrl${httpRequest.endPoint}"
        val url = URL(baseUrl)
        val httpConn = url.openConnection()
        httpRequest.headers.forEach {
            httpConn.setRequestProperty(it.key, it.value)
        }

        httpConn.doOutput = true

        httpConn.outputStream.use {
            it.write(parameters.toByteArray(Charsets.UTF_8))
            it.flush()
            it.close()
        }

        val response = StringBuilder()

        val rc = (httpConn as HttpURLConnection).responseCode
        if (rc != 201 && rc != 200) {
            val errorObject = JSONObject()
            errorObject.put("error", rc)
            throw Exception(errorObject.toString())
        }

        BufferedReader(InputStreamReader(httpConn.inputStream, Charsets.UTF_8)).use { br ->
            var responseLine: String?
            responseLine = br.readLine()
            while (responseLine != null) {
                response.append(responseLine.trim())
                responseLine = br.readLine()
            }
        }
        return response.toString()
    }

    private fun createRequestBuilder(httpRequest: HttpRequest): Request {
        val url = "$baseUrl${httpRequest.getEndPointWithQueryParameters()}"
        val builder = when (httpRequest.method) {
            HttpRequest.Method.GET -> {
                Request.Builder()
                    .url(url)
                    .get()
            }
            HttpRequest.Method.POST -> {
                Request.Builder()
                    .url(url)
                    .post(
                        httpRequest
                            .body
                            .toRequestBody(
                                httpRequest.bodyMediaType.toMediaTypeOrNull()
                            )
                    )
            }
        }

        httpRequest.headers.forEach {
            builder.addHeader(it.key, it.value)
        }
        return builder.build()
    }

    companion object {
        private const val THREAD_POOL_SIZE = 5
    }
}