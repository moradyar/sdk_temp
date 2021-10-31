package com.moradyar.carouselfeature.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moradyar.carouselfeature.core.model.FeedResult
import com.moradyar.carouselfeature.impl.FeedRequestState

class CarouselViewModel(
    private val feedRepository: FeedRepository
) : ViewModel() {

    private val _loadData = MutableLiveData<FeedViewState>()
    val loadData: LiveData<FeedViewState> = _loadData

    fun onCreate() {
        feedRepository.fetchDiscoveryFeed { state ->
            val data = when (state) {
                is FeedRequestState.Error -> FeedViewState.Error("${state.e.message}")
                FeedRequestState.Loading -> FeedViewState.Loading
                is FeedRequestState.Success -> {
                    when (val res = state.value) {
                        is FeedResult.Error -> FeedViewState.Error(res.error)
                        FeedResult.FeedExpired -> FeedViewState.Error("Expired")
                        FeedResult.FeedOver -> FeedViewState.Error("Over")
                        is FeedResult.Videos -> FeedViewState.LoadData(res)
                    }
                }
            }
            _loadData.postValue(data)
        }

    }

    fun onReachToEndOfTheList(feedId: String, nextCursorId: String) {
        feedRepository.fetchDiscoveryFeedNextPage(
            feedId = feedId,
            nextCursorId = nextCursorId
        ) { state ->
            val data = when (state) {
                is FeedRequestState.Error -> FeedViewState.Error("${state.e.message}")
                FeedRequestState.Loading -> FeedViewState.Loading
                is FeedRequestState.Success -> {
                    when (val res = state.value) {
                        is FeedResult.Error -> FeedViewState.Error(res.error)
                        FeedResult.FeedExpired -> FeedViewState.Error("Expired")
                        FeedResult.FeedOver -> FeedViewState.Error("Over")
                        is FeedResult.Videos -> FeedViewState.AppendData(res)
                    }
                }
            }
            _loadData.postValue(data)
        }
    }

    fun onDestroy() {
        feedRepository.clearData()
    }
}