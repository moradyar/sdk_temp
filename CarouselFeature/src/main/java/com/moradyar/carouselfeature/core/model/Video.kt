package com.moradyar.carouselfeature.core.model

data class Video(
    var id: String,
    var badge: String? = null,
    //var unlock_time: Float? = null,
    //var format: String? = null,
    //var reposts_url: String? = null,
    //var reposts_count: Int = 0,
    //var download_url: String? = null,
    var file_url: String,
    var vast_tag: String? = null,
    //var comments_url: String? = null,
    //var likes_url: String? = null,
    //var views_url: String? = null,
    ///var shares_url: String? = null,
    //var report_url: String? = null,
    var engagements_url: String? = null,
    //var dislikes_url: String? = null,
    var url: String? = null,
    //var ref_video_url: String? = null,
    //var ref_video_id: Int = 0,
    //var is_following_creator: Boolean = false,
    var encoded_id: String = "",
    var video_type: String? = null,
    //var access: String? = null,
    var caption: String? = null,
    //var status: String? = null,
    //var is_liked: Boolean = false,
    //var inserted_at: String? = null,
    var variant: String? = null,
    var creator: Creator? = null,
    //var comments_count: Int = 0,
    //var is_reposted: Boolean = false,
    var web_share_url: String? = null,
    var width: Int = 0,
    var height: Int = 0,
    //var hashtags: List<String>? = null,
    var likes_count: Int = 0,
    //var shares_count: Int = 0,
    var views_count: Int = 0,
    var thumbnail_url: String?,
    var viewed: Int = 0,
    //var timeModified: Long = System.nanoTime(),
    // var quality: String? = null,
    var reveal_type: String? = null,
    var subVariant: String? = null,
    var action_type: String? = null,
    var action_type_translation: String? = null,
    var action_url: String? = null,
    var trackUrl: String? = null,
    var autoPlay: Boolean = false,
    val videoPosters: List<Poster>?,
    val aHashtags: Array<String?>?,
    val products: List<Product>? = null
) {

    val frameless = video_type == "frameless"
    var reported: Boolean = false
    var duration = 0.0f
    var played = false
    var playerUrl: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Video

        if (id != other.id) return false
        if (encoded_id != other.encoded_id) return false
        if (autoPlay != other.autoPlay) return false
        return true
    }
}