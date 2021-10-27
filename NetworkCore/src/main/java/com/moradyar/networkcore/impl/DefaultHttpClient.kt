package com.moradyar.networkcore.impl

import com.moradyar.networkcore.core.HttpClient

/**
 * The default impl of HttpClient which is internal and invisible from the outside of the module
 * This can be replaced by other impls in the future
 */
internal class DefaultHttpClient : HttpClient {

    override fun get(url: String): String {
        return "Example result"
    }
}