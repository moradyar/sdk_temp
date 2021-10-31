package com.moradyar.utilitycore.impl

import android.os.Build
import com.moradyar.utilitycore.core.UserAgentInfoHelper
import java.net.URLEncoder

internal class DefaultUserAgentInfoHelper(
    private val environmentInfoProvider: EnvironmentInfoProvider
) : UserAgentInfoHelper {

    override fun getUserAgentInfo(): String {
        val productName = getProductName()
        val sb = StringBuilder()
        sb.append(System.getProperty("http.agent") ?: "")
        sb.append("[FWAN/$productName;")
        sb.append("FWCA/${environmentInfoProvider.getApplicationName()};")
        sb.append("FWAV/${FIREWORK_SDK_VERSION};")
        sb.append("FWCN/${environmentInfoProvider.getPackageName()};")
        sb.append("FWCV/${environmentInfoProvider.getAppInfo()};")
        sb.append("FWCR/${environmentInfoProvider.getDeviceCarrier()};")
        sb.append("FWDV/${Build.MODEL};")
        sb.append("FWLC/${environmentInfoProvider.getLanguage()};")
        sb.append("FWMD/${Build.MANUFACTURER};")
        sb.append("FWSN/${environmentInfoProvider.getOS()};")
        sb.append("FWBI/${environmentInfoProvider.getPackageName()};")
        sb.append("FWSV/${environmentInfoProvider.getAndroidVersion()}]")
        return URLEncoder.encode(sb.toString(), "utf-8").replace("+", "%20")
    }

    private fun getProductName(): String {
        return "embed.android.sdk"
    }

    companion object {
        private const val FIREWORK_SDK_VERSION = "v5.6.0"
    }
}