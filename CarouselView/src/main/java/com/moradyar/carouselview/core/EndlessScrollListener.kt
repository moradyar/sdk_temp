package com.moradyar.carouselview.core

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EndlessScrollListener(
    private val onLastItemReached: () -> Unit
) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 5
    private var firstVisibleItem = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.childCount
        totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
        firstVisibleItem = (recyclerView.layoutManager as? LinearLayoutManager)
            ?.findFirstVisibleItemPosition()
            ?: 0

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
            <= (firstVisibleItem + visibleThreshold)
        ) {
            onLastItemReached()
            loading = true
        }
    }
}