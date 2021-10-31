package com.moradyar.carouselfeature.core.model

sealed class FeedResult {
    data class Error(val error: String) : FeedResult()
    object FeedOver : FeedResult()
    object FeedExpired : FeedResult()
    data class Videos(
        val feedId: String,
        val nextCursor: String? = null,
        val videos: List<Video>
    ) : FeedResult()
}