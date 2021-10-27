package com.moradyar.gridfeedview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.moradyar.feedprovidingenabler.core.FeedViewModel
import com.moradyar.sdkinitializingenabler.core.FireworkSdk

/**
 * This is the custom view for grid feed view
 */
class GridFeedView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var feedViewModel: FeedViewModel = FireworkSdk.getFeedViewModel(context)
}