package com.moradyar.carouselview.core

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.moradyar.carouselfeature.core.model.Video
import com.moradyar.carouselview.R
import com.moradyar.imageloadingcore.core.ImageLoader

internal class CarouselAdapter(
    private val context: Context,
    private val imageLoader: ImageLoader
) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    private val items = mutableListOf<Video>()
    var feedId: String = ""
    var nextCursor: String = ""

    fun setItems(items: List<Video>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun append(items: List<Video>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        return CarouselViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.carousel_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.title.text = items[position].caption
        imageLoader.load(items[position].thumbnail_url ?: "", holder.image)
    }

    override fun getItemCount(): Int = items.size

    class CarouselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val image: ImageView = itemView.findViewById(R.id.thumbnail)
    }
}