package com.moradyar.sdkarchitecturetemplate

import android.app.Application
import com.moradyar.sdkinitializingenabler.core.FireworkSdk

class RootApp : Application() {

    override fun onCreate() {
        super.onCreate()
        FireworkSdk.inti(
            hostClientId = "HostClientId",
            hostUserId = "HostUserId"
        )
    }
}