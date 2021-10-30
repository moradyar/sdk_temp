package com.moradyar.utilitycore.core

import com.moradyar.utilitycore.impl.DefaultUniqueIdProvider

interface UniqueIdProvider {

    fun getUUID(): String

    object Factory {
        fun getInstance(): UniqueIdProvider {
            return DefaultUniqueIdProvider()
        }
    }
}