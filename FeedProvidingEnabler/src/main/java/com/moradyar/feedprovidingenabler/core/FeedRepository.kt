package com.moradyar.feedprovidingenabler.core

import com.moradyar.feedprovidingenabler.impl.DefaultFeedRepository
import com.moradyar.networkcore.core.HttpClient

/**
 * This is the feed repository interface which
 * is visible from the outside of the module
 */
interface FeedRepository {

    /**
     * Add each video feed function we need here
     */
    fun getDiscoveryVideoFeed(): List<String>

    object Factory {
        fun getInstance(httpClient: HttpClient): FeedRepository {
            return DefaultFeedRepository(httpClient)
        }
    }
}