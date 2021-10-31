package com.moradyar.carouselfeature.core

import com.moradyar.carouselfeature.core.model.FeedResult

sealed class FeedViewState {

    object Loading : FeedViewState()
    class Error(val message: String) : FeedViewState()
    class LoadData(val feed: FeedResult.Videos) : FeedViewState()
    class AppendData(val feed: FeedResult.Videos) : FeedViewState()
}