package com.moradyar.utilitycore.impl

internal interface EnvironmentInfoProvider {

    fun getApplicationName(): String

    fun getPackageName(): String

    fun getAppInfo(): String?

    fun getDeviceCarrier(): String

    fun getLanguage(): String

    fun getOS(): String

    fun getAndroidVersion(): String
}