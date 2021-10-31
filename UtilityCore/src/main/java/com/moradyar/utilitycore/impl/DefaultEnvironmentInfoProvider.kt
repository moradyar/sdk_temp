package com.moradyar.utilitycore.impl

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.telephony.TelephonyManager
import java.util.*

internal class DefaultEnvironmentInfoProvider(
    private val context: Context
) : EnvironmentInfoProvider {

    override fun getApplicationName(): String {
        val applicationInfo =
            context.packageManager.getApplicationInfo(context.applicationInfo.packageName, 0)
        return context.packageManager.getApplicationLabel(applicationInfo).toString()
    }

    override fun getPackageName(): String {
        return context.applicationContext.packageName
    }

    override fun getAppInfo(): String? {
        val pInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return pInfo.versionName
    }

    override fun getDeviceCarrier(): String {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.networkOperatorName
    }

    override fun getLanguage(): String {
        return Locale.getDefault().toLanguageTag()
    }

    override fun getOS(): String {
        return "android"
    }

    override fun getAndroidVersion(): String {
        return Build.VERSION.RELEASE
    }
}