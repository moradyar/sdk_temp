package com.moradyar.carouselfeature.impl

import com.moradyar.carouselfeature.core.model.FeedResult

sealed class FeedRequestState {
    object Loading : FeedRequestState()

    class Success(val value: FeedResult) : FeedRequestState()

    class Error(val e: Exception) : FeedRequestState()
}
