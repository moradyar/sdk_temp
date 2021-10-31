package com.moradyar.sdkarchitecturetemplate

import android.app.Application
import com.moradyar.sdkinitializer.FireworkSdk

class RootApp : Application() {

    private val clientId by lazy {
        "f6d6ec1275217f178cdff91363825cb390e038c1168f64f6efa23cb95ec6b325"
    }
    private val guestUserId: String by lazy {
        "123jdk"
    }

    override fun onCreate() {
        super.onCreate()
        FireworkSdk.init(clientId, guestUserId)
    }
}