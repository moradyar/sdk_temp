package com.moradyar.networkcore.core

import com.moradyar.networkcore.impl.DefaultHttpClient

/*
This interface is public and is visible from the outside of the module
 */
interface HttpClient {

    fun get(
        endPoint: String,
        headers: Map<String, String>? = null,
        queryParameters: Map<String, String>? = null,
        useCache: Boolean = false
    ): String

    fun post(
        endPoint: String,
        body: String? = null,
        headers: Map<String, String>? = null,
        queryParameters: Map<String, String>? = null,
        useCache: Boolean = false
    ): String

    /*
    Factory object
     */
    object Factory {

        fun getInstance(): HttpClient {
            return DefaultHttpClient("")
        }
    }
}