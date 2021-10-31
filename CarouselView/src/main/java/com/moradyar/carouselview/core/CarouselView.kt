package com.moradyar.carouselview.core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moradyar.carouselfeature.core.CarouselViewModel
import com.moradyar.carouselfeature.core.FeedViewState
import com.moradyar.carouselview.R
import com.moradyar.sdkinitializer.FireworkSdk

class CarouselView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewModel.onCreate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewModel.onDestroy()
    }

    private val viewModel: CarouselViewModel = FireworkSdk.carouselViewModel(context)
    private val adapter = CarouselAdapter(context, FireworkSdk.imageLoader())

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.carousel_view, null, false)
        addView(view)
        val rv = findViewById<RecyclerView>(R.id.rv)
        val prg = findViewById<ProgressBar>(R.id.prg)
        val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv.layoutManager = lm
        rv.adapter = adapter
        rv.isHorizontalScrollBarEnabled = true
        rv.addOnScrollListener(EndlessScrollListener {
            viewModel.onReachToEndOfTheList(adapter.feedId, adapter.nextCursor)
        })
        (context as? AppCompatActivity)?.let { owner ->
            viewModel.loadData.observe(owner) {
                when (it) {
                    is FeedViewState.Error -> prg.isVisible = false
                    is FeedViewState.LoadData -> {
                        prg.isVisible = false
                        adapter.feedId = it.feed.feedId
                        adapter.nextCursor = it.feed.nextCursor ?: ""
                        adapter.setItems(it.feed.videos)
                    }
                    FeedViewState.Loading -> prg.isVisible = true
                    is FeedViewState.AppendData -> {
                        prg.isVisible = false
                        adapter.nextCursor = it.feed.nextCursor ?: ""
                        adapter.append(it.feed.videos)
                    }
                }
            }
        }

    }
}