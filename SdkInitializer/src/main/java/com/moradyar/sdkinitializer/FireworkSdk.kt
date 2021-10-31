package com.moradyar.sdkinitializer

import android.content.Context
import com.moradyar.authenticationfeature.core.Authenticator
import com.moradyar.carouselfeature.core.CarouselViewModel
import com.moradyar.carouselfeature.core.FeedRepository
import com.moradyar.imageloadingcore.core.ImageLoader
import com.moradyar.networkcore.core.HttpClient
import com.moradyar.utilitycore.core.UniqueIdProvider
import com.moradyar.utilitycore.core.UserAgentInfoHelper

object FireworkSdk {

    private var hostClientId: String = ""
    private var hostUserId: String? = null

    fun init(clientId: String, guestUserId: String?) {
        hostClientId = clientId
        hostUserId = guestUserId
    }

    fun carouselViewModel(context: Context): CarouselViewModel {
        return CarouselViewModel(feedRepository(context))
    }

    fun imageLoader(): ImageLoader {
        return ImageLoader.Factory.getInstance()
    }

    private fun feedRepository(context: Context): FeedRepository {
        val httpClient = httpClient()
        return FeedRepository.Factory.getInstance(httpClient, authenticator(context, httpClient))
    }

    private fun authenticator(context: Context, httpClient: HttpClient): Authenticator {
        return Authenticator.Factory.getInstance(
            httpClient = httpClient,
            clientId = hostClientId,
            guestUserId = hostUserId ?: "",
            userAgentInfoHelper = userAgentInfoHelper(context),
            uniqueIdProvider = uniqueIdProvider()
        )
    }

    private fun uniqueIdProvider(): UniqueIdProvider {
        return UniqueIdProvider.Factory.getInstance()
    }

    private fun userAgentInfoHelper(context: Context): UserAgentInfoHelper {
        return UserAgentInfoHelper.Factory.getInstance(context)
    }

    private fun httpClient(): HttpClient {
        return HttpClient.Factory.getInstance(baseUrl())
    }

    private fun baseUrl(): String {
        return "https://api.fw.tv"
    }
}