package com.moradyar.utilitycore.impl

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import com.moradyar.utilitycore.core.UserAgentInfoHelper
import java.net.URLEncoder
import java.util.*

internal class DefaultUserAgentInfoHelper(
    private val context: Context
) : UserAgentInfoHelper {

    override fun getUserAgentInfo(): String {
        val productName = getProductName()
        val sb = StringBuilder()
        sb.append(System.getProperty("http.agent") ?: "")
        sb.append("[FWAN/$productName;")
        sb.append("FWCA/${context.getApplicationName()};")
        sb.append("FWAV/${FIREWORK_SDK_VERSION};")
        sb.append("FWCN/${context.applicationContext.packageName};")
        sb.append("FWCV/${context.getAppInfo()};")
        sb.append("FWCR/${context.getDeviceCarrier()};")
        sb.append("FWDV/${Build.MODEL};")
        sb.append("FWLC/${getLanguage()};")
        sb.append("FWMD/${Build.MANUFACTURER};")
        sb.append("FWSN/${getOS()};")
        sb.append("FWBI/${context.packageName};")
        sb.append("FWSV/${getAndroidVersion()}]")
        Log.i("TTTT", sb.toString())
        return URLEncoder.encode(sb.toString(), "utf-8").replace("+", "%20")
    }

    private fun Context.getApplicationName(): String {
        val applicationInfo = packageManager.getApplicationInfo(applicationInfo.packageName, 0)
        return packageManager.getApplicationLabel(applicationInfo).toString()
    }

    private fun Context.getAppInfo(): String? {
        val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
        return pInfo.versionName
    }

    private fun Context.getDeviceCarrier(): String {
        val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.networkOperatorName
    }

    private fun getLanguage(): String {
        return Locale.getDefault().toLanguageTag()
    }

    private fun getOS(): String {
        return "android"
    }

    private fun getAndroidVersion(): String {
        return Build.VERSION.RELEASE
    }

    private fun getProductName(): String {
        return "embed.android.sdk"
    }

    companion object {
        private const val FIREWORK_SDK_VERSION = "v5.6.0"
    }
}