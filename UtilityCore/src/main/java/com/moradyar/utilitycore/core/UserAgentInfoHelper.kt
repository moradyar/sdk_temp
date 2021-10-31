package com.moradyar.utilitycore.core

import android.content.Context
import com.moradyar.utilitycore.impl.DefaultEnvironmentInfoProvider
import com.moradyar.utilitycore.impl.DefaultUserAgentInfoHelper

interface UserAgentInfoHelper {

    fun getUserAgentInfo(): String

    object Factory {
        fun getInstance(
            context: Context
        ): UserAgentInfoHelper {
            val environmentInfoProvider = DefaultEnvironmentInfoProvider(context)
            return DefaultUserAgentInfoHelper(environmentInfoProvider)
        }
    }
}