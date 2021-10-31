package com.moradyar.imageloadingcore.core

import android.widget.ImageView
import com.moradyar.imageloadingcore.impl.DefaultImageLoader

interface ImageLoader {

    fun load(imageUrl: String, imageView: ImageView)

    object Factory {
        fun getInstance(): ImageLoader {
            return DefaultImageLoader()
        }
    }
}