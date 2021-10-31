package com.moradyar.carouselfeature.impl

import com.moradyar.carouselfeature.core.model.*
import org.json.JSONObject

internal object JsonDeserializer {

    fun deserializeFeedResult(jsonString: String): FeedResult {
        if (jsonString.isNullOrEmpty()) {
            return FeedResult.Error("")
        }

        val feedObject = JSONObject(jsonString)
        val dataObject = feedObject.getJSONObject("data")

        val discoverObject =
            if (dataObject.has("createDiscoverFeed")) {
                dataObject.getJSONObject("createDiscoverFeed")
            } else if (dataObject.has("createChannelPersonalizedFeed")) {
                dataObject.getJSONObject("createChannelPersonalizedFeed")
            } else if (dataObject.has("createChannelPlaylistFeed")) {
                dataObject.getJSONObject("createChannelPlaylistFeed")
            } else {
                dataObject.getJSONObject("feed")
            }

        if (discoverObject.has("message")) {
            val message = discoverObject.optString("message") ?: ""

            if (message == "Feed not found.") {
                return FeedResult.FeedExpired
            }
            return FeedResult.Error(message)
        }
        if (!discoverObject.has("id")) {
            return FeedResult.FeedExpired
        }

        val itemsConnection = discoverObject.getJSONObject("itemsConnection")
        val pageInfoObject = itemsConnection.getJSONObject("pageInfo")
        val nextCursorId = pageInfoObject.optString("endCursor")
        val feedOver = nextCursorId == null || nextCursorId == "null"

        val edges = itemsConnection.getJSONArray("edges")

        if (feedOver && edges.length() <= 0) {
            return FeedResult.FeedOver
        }

        val videos = ArrayList<Video>()
        for (i in 0 until edges.length()) {
            val edgeObject = edges.getJSONObject(i)
            val nodeObject = edgeObject.optJSONObject("node")
            nodeObject?.let { nodeObject ->
                val badge = nodeObject.optString("badge")
                var caption = nodeObject.optString("caption")
                if (caption == "null") {
                    caption = ""
                }
                val duration = nodeObject.optDouble("duration")
                val engagementUrl = nodeObject.optString("engagementUrl")
                // val height = nodeObject.optInt("height")
                //  val width = nodeObject.optInt("width")
                val id = nodeObject.optString("id")
                val revealType = nodeObject.optString("revealType")
                val sharesCount = nodeObject.optInt("sharesCount")
                val thumbnailUrl = nodeObject.optString("thumbnailUrl")
                val videoType = nodeObject.optString("videoType")
                val viewsCount = nodeObject.optInt("viewsCount", 1)
                val webShareUrl = nodeObject.optString("webShareUrl")
                val variant = edgeObject.optString("variant")
                val creatorObject = nodeObject.optJSONObject("creator")
                val avatarUrl = creatorObject?.optString("avatarUrl")
                val name = creatorObject?.optString("name")
                val username = creatorObject?.optString("username")
                val creatorId = creatorObject?.optString("id")
                val creator = Creator(avatarUrl, name, null, username, creatorId)

                var width = 0
                var height = 0
                var fileUrl = ""

                val products = ArrayList<Product>()

                nodeObject.optJSONArray("products")?.apply {
                    for (i in 0 until this.length()) {
                        val product = this.getJSONObject(i)
                        val currency = product.optString("currency")
                        val productId = product.optString("id")
                        val productName = product.optString("name", "")
                        val description = product.optString("description", "")
                        val optionJson = product.optJSONArray("options")
                        val productOptions = ArrayList<String>()

                        optionJson.apply {
                            for (i in 0 until this.length()) {
                                val option = this.getString(i)
                                productOptions.add(option)
                            }
                        }

                        val images = ArrayList<ProductImage>()

                        product.optJSONArray("images")?.apply {

                            for (i in 0 until this.length()) {
                                val image = this.getJSONObject(i)
                                val id = image?.optString("id")
                                val position = image?.optInt("position", -1)
                                val url = image?.optString("url")
                                images.add(ProductImage(id, url, position))
                            }
                        }

                        val units = ArrayList<ProductUnit>()

                        product.optJSONArray("units")?.apply {

                            for (i in 0 until this.length()) {
                                val unit = this.getJSONObject(i)
                                val id = unit?.optString("id")
                                val position = unit?.optInt("position", -1)
                                val price = unit?.optString("price")
                                val url = unit?.optString("url")
                                units.add(ProductUnit(id, name, position, price, url))
                            }
                        }
                        products.add(
                            Product(
                                productId,
                                productName,
                                currency,
                                description,
                                productOptions,
                                images,
                                units
                            )
                        )
                    }
                }

                nodeObject.optJSONArray("videoFiles")?.apply {
                    for (i in 0 until this.length()) {
                        val videoFile = this.getJSONObject(i)
                        val format = videoFile.optString("format")
                        if (format == "hevc") {
                            width = videoFile?.optInt("width", 0) ?: 0
                            height = videoFile?.optInt("height", 0) ?: 0
                            fileUrl = videoFile?.optString("fileUrl") ?: ""
                        }
                    }
                }

                val videoPosterArray = nodeObject.optJSONArray("videoPosters")
                val posters = ArrayList<Poster>()

                videoPosterArray?.let {
                    for (i in 0 until it.length()) {
                        val videoPosterObject = it.getJSONObject(i)
                        val posterId = videoPosterObject?.optString("id")
                        val posterFormat = videoPosterObject?.optString("format")
                        val posterUrl = videoPosterObject?.optString("url")
                        val poster = Poster(null, posterFormat, posterId, posterUrl, null)
                        posters.add(poster)
                    }
                }

                val callToActionObject = nodeObject.optJSONObject("callToAction")
                val trackUrl =
                    callToActionObject?.optString("trackUrl") //?: "https://fireworktv.com"
                val type = callToActionObject?.optString("type") //?: "View"
                val typeTranslation = callToActionObject?.optString("typeTranslation")
                val actionUrl = callToActionObject?.optString("url") //?: "https://fireworktv.com"

                val hashtags = nodeObject.optJSONArray("hashtags")
                val aHashtags: Array<String?> = arrayOfNulls(hashtags?.length() ?: 0)

                hashtags?.let { hashtags ->
                    for (k in 0 until hashtags.length()) {
                        aHashtags[k] = (hashtags.getString(k))
                    }
                }

                if (videoType != "live_stream" && fileUrl.isNotEmpty()) {
                    videos.add(
                        Video(
                            "",
                            badge,
                            fileUrl,
                            null,
                            engagementUrl,
                            null,
                            id,
                            videoType,
                            caption,
                            variant,
                            creator,
                            webShareUrl,
                            width,
                            height,
                            0,
                            viewsCount,
                            thumbnailUrl,
                            0,
                            null,
                            revealType,
                            type,
                            typeTranslation,
                            actionUrl,
                            trackUrl,
                            false,
                            posters,
                            aHashtags,
                            products
                        )
                    )
                }
            }
        }

        val feedId = if (discoverObject.has("id")) {
            discoverObject.optString("id")
        } else {
            ""
        }

        return FeedResult.Videos(feedId, nextCursorId, videos)
    }
}