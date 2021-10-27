package com.moradyar.networkcore.core

import com.moradyar.networkcore.impl.DefaultHttpClient

/*
This interface is public and is visible from the outside of the module
 */
interface HttpClient {

    /*
    Put all core network functions here
    This function is just an example
     */
    fun get(url: String): String

    /*
    Factory object
     */
    object Factory {

        fun getInstance(): HttpClient {
            return DefaultHttpClient()
        }
    }
}