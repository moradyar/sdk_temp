package com.moradyar.carouselfeature.core

import com.moradyar.authenticationfeature.core.Authenticator
import com.moradyar.carouselfeature.impl.FeedRepositoryImpl
import com.moradyar.carouselfeature.impl.FeedRequestState
import com.moradyar.networkcore.core.HttpClient

interface FeedRepository {

    fun fetchDiscoveryFeed(pageSize: Int = 20, onFeedReady: (FeedRequestState) -> Unit)

    fun fetchDiscoveryFeedNextPage(
        pageSize: Int = 20,
        feedId: String? = null,
        nextCursorId: String? = null,
        onFeedReady: (FeedRequestState) -> Unit
    )

    fun setPlayedVideoIndex(videoIndex: Int)

    fun clearData()

    object Factory {
        fun getInstance(httpClient: HttpClient, authenticator: Authenticator): FeedRepository {
            return FeedRepositoryImpl(httpClient, authenticator)
        }
    }
}