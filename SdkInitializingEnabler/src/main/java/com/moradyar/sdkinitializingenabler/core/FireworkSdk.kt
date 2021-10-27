package com.moradyar.sdkinitializingenabler.core

import android.content.Context
import com.moradyar.feedprovidingenabler.core.FeedRepository
import com.moradyar.feedprovidingenabler.core.FeedViewModel
import com.moradyar.networkcore.core.HttpClient

object FireworkSdk {

    private var clientId: String = ""
    private var userId: String? = null

    fun inti(
        hostClientId: String,
        hostUserId: String?
    ) {
        clientId = hostClientId
        userId = hostUserId
        // TODO do whatever need for initializing the SDK
    }

    fun getFeedViewModel(context: Context): FeedViewModel {
        val feedRepository = getFeedRepository()
        return FeedViewModel(feedRepository)
    }

    private fun getFeedRepository(): FeedRepository {
        val httpClient = getHttpClient()
        return FeedRepository.Factory.getInstance(httpClient)
    }

    private fun getHttpClient(): HttpClient {
        return HttpClient.Factory.getInstance()
    }
}