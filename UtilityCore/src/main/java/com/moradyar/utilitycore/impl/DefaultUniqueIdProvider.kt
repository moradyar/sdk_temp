package com.moradyar.utilitycore.impl

import com.moradyar.utilitycore.core.UniqueIdProvider
import java.util.*

internal class DefaultUniqueIdProvider : UniqueIdProvider {

    override fun getUUID(): String {
        if (uuid.isEmpty()) {
            uuid = UUID.randomUUID().toString()
        }
        return uuid
    }

    companion object {
        private var uuid = ""
    }
}