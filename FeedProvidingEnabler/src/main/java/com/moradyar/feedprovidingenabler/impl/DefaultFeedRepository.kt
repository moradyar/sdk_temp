package com.moradyar.feedprovidingenabler.impl

import com.moradyar.feedprovidingenabler.core.FeedRepository
import com.moradyar.networkcore.core.HttpClient

/**
 * The default impl of feed repo which is
 * invisible from the outside of the module
 * It needs an instance of HttpClient interface in constructor
 */
internal class DefaultFeedRepository(
    private val httpClient: HttpClient
) : FeedRepository {

    override fun getDiscoveryVideoFeed(): List<String> {
        return listOf(httpClient.get("TestUrl"))
    }
}