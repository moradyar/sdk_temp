package com.moradyar.imageloadingcore.impl

import android.widget.ImageView
import com.moradyar.imageloadingcore.core.ImageLoader
import com.squareup.picasso.Picasso

internal class DefaultImageLoader : ImageLoader {

    private val picasso = Picasso.get()

    override fun load(imageUrl: String, imageView: ImageView) {
        picasso.load(imageUrl)
            .into(imageView)
    }
}