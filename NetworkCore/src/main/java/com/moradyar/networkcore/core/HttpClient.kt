package com.moradyar.networkcore.core

import com.moradyar.networkcore.impl.DefaultHttpClient

interface HttpClient {

    fun fetch(
        httpRequest: HttpRequest,
        onResponseReady: (response: RequestState) -> Unit
    )

    fun fetchRaw(
        httpRequest: HttpRequest,
        onResponseReady: (response: RequestState) -> Unit
    )

    object Factory {

        fun getInstance(baseUrl: String): HttpClient {
            return DefaultHttpClient(baseUrl)
        }
    }
}