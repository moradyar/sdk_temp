package com.moradyar.feedprovidingenabler.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel

class FeedViewModel(
    private val feedRepository: FeedRepository
) : ViewModel() {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onViewCreated() {
        val listOfItems = feedRepository.getDiscoveryVideoFeed()
        // Do something with the list of feed
    }
}