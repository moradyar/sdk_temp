package com.moradyar.networkcore.impl

import com.moradyar.networkcore.core.HttpClient
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * The default impl of HttpClient which is internal and invisible from the outside of the module
 * This can be replaced by other impls in the future
 */
internal class DefaultHttpClient(
    private val baseUrl: String
) : HttpClient {

    override fun get(
        endPoint: String,
        headers: Map<String, String>?,
        queryParameters: Map<String, String>?,
        useCache: Boolean
    ): String {
        return "Test string"
//        val url = createUrl(endPoint, queryParameters)
//        val connection = URL(url)
//        val response = StringBuffer()
//        with(connection.openConnection() as HttpURLConnection) {
//            requestMethod = GET
//            setHeaders(headers)
//            useCaches = useCache
//
//            BufferedReader(InputStreamReader(inputStream)).use {
//                var inputLine = it.readLine()
//                while (inputLine != null) {
//                    response.append(inputLine)
//                    inputLine = it.readLine()
//                }
//                it.close()
//            }
//        }
//        return response.toString()
    }

    override fun post(
        endPoint: String,
        body: String?,
        headers: Map<String, String>?,
        queryParameters: Map<String, String>?,
        useCache: Boolean
    ): String {
        val url = createUrl(endPoint, queryParameters)
        val connection = URL(url)
        val response = StringBuffer()
        with(connection.openConnection() as HttpURLConnection) {
            requestMethod = POST
            setHeaders(headers)
            useCaches = useCache

            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
            }
        }
        return response.toString()
    }

    private fun createQueryParameterFromMap(parameters: Map<String, String>?): String {
        return parameters?.entries?.map { entry ->
            "${encode(entry.key)}=${encode(entry.value)}"
        }?.joinToString(encode("&")) { it } ?: ""
    }

    private fun encode(value: String): String {
        return URLEncoder.encode(value, ENCODING)
    }

    private fun HttpURLConnection.setHeaders(headers: Map<String, String>?) {
        headers?.entries?.forEach {
            setRequestProperty(it.key, it.value)
        }
    }

    private fun createUrl(
        endpoint: String,
        queryParameters: Map<String, String>?
    ): String {
        val parameters = createQueryParameterFromMap(queryParameters)
        val fullUrl = "$baseUrl/$endpoint"
        val url = if (parameters.isEmpty()) {
            fullUrl
        } else {
            "$fullUrl?$parameters"
        }
        return url
    }

    companion object {
        private const val ENCODING = "UTF-8"
        private const val GET = "GET"
        private const val POST = "POST"
    }
}