package com.moradyar.carouselfeature.impl

import com.moradyar.authenticationfeature.core.Authenticator
import com.moradyar.carouselfeature.core.FeedRepository
import com.moradyar.carouselfeature.core.model.FeedResult
import com.moradyar.carouselfeature.core.model.Video
import com.moradyar.networkcore.core.HttpClient
import com.moradyar.networkcore.core.HttpRequest
import com.moradyar.networkcore.core.RequestState

internal class FeedRepositoryImpl(
    private val httpClient: HttpClient,
    private val authenticator: Authenticator
) : FeedRepository {

    override fun fetchDiscoveryFeed(pageSize: Int, onFeedReady: (FeedRequestState) -> Unit) {
        fetch(discoveryFeed(20), onFeedReady)
    }

    override fun fetchDiscoveryFeedNextPage(
        pageSize: Int,
        feedId: String?,
        nextCursorId: String?,
        onFeedReady: (FeedRequestState) -> Unit
    ) {
        fetch(nextPageDiscoveryFeed(20, feedId, nextCursorId), onFeedReady)
    }

    private fun fetch(body: String, onFeedReady: (FeedRequestState) -> Unit) {
        onFeedReady(FeedRequestState.Loading)
        authenticator.getRefreshedToken { token ->
            val headers = mapOf(
                ACCEPT_LANGUAGE_HEADER_KEY to ACCEPT_LANGUAGE_HEADER_VALUE,
                AUTHORIZATION_HEADER_KEY to token.authToken,
                USER_AGENT_HEADER_KEY to token.userAgent,
                SESSION_id_HEADER_KEY to token.sessionId
            )
            val httpRequest = HttpRequest.Builder(FEED_GRAPH_QL_ENDPOINT)
                .headers(headers)
                .body(body)
                .useCache(false)
                .method(HttpRequest.Method.POST)
                .bodyMediaType(REQUEST_BODY_GRAPH_QL)
                .build()
            httpClient.fetch(httpRequest) { state ->
                when (state) {
                    is RequestState.Error -> {
                        onFeedReady(FeedRequestState.Error(state.e))
                    }
                    is RequestState.Success -> {
                        try {
                            val result: FeedResult =
                                JsonDeserializer.deserializeFeedResult(state.value)
                            if (result is FeedResult.Videos) {
                                addToCache(result.videos)
                            }
                            onFeedReady(FeedRequestState.Success(result))
                        } catch (e: Exception) {
                            e.printStackTrace()
                            onFeedReady(FeedRequestState.Error(e))
                        }
                    }
                }
            }
        }
    }

    private fun addToCache(list: List<Video>) {
        videoFeeds.addAll(list)
    }

    override fun setPlayedVideoIndex(videoIndex: Int) {
        if (videoIndex > 0) {
            latestPlayedIndex = videoIndex
        }
    }

    override fun clearData() {
        videoFeeds.clear()
    }

    companion object {

        private val videoFeeds = mutableListOf<Video>()
        private var latestPlayedIndex: Int = 0

        private const val ACCEPT_LANGUAGE_HEADER_KEY = "Accept-Language"
        private const val ACCEPT_LANGUAGE_HEADER_VALUE = "en-US"
        private const val AUTHORIZATION_HEADER_KEY = "Authorization"
        private const val USER_AGENT_HEADER_KEY = "User-Agent"
        private const val SESSION_id_HEADER_KEY = "session_id"
        private const val FEED_GRAPH_QL_ENDPOINT = "/graphiql"
        private const val REQUEST_BODY_GRAPH_QL = "application/graphql"

        private fun discoveryFeed(pageSize: Int) = "mutation {\n" +
                "      createDiscoverFeed {\n" +
                "      ... on Feed { \n" +
                "         id\n" +
                "        itemsConnection(first: $pageSize) {\n" +
                "          pageInfo {\n" +
                "    \t\t\t\tstartCursor\n" +
                "            endCursor\n" +
                "            hasPreviousPage\n" +
                "            hasNextPage\n" +
                "          }\n" +
                "          edges {\n" +
                "            cursor\n" +
                "            variant\n" +
                "            node {\n" +
                "              ... on Video {\n" +
                "                badge\n" +
                "                engagementUrl\n" +
                "                id\n" +
                "                videoType\n" +
                "                videoPosters {\n" +
                "                  id\n" +
                "                  format\n" +
                "                  url\n" +
                "                }\n" +
                "                creator {\n" +
                "                  id\n" +
                "                  name\n" +
                "                  username\n" +
                "                  avatarUrl\n" +
                "                }\n" +
                "                products {\n" +
                "                  id\n" +
                "                  name\n" +
                "                  currency\n" +
                "                  options\n" +
                "                  description\n" +
                "                  images {\n" +
                "                  id\n" +
                "                  position\n" +
                "                  url\n" +
                "                  }\n" +
                "                  units {\n" +
                "                  id\n" +
                "                  name\n" +
                "                  position\n" +
                "                  price\n" +
                "                  url\n" +
                "                  }\n" +
                "                }\n" +
                "                videoFiles {\n" +
                "                fileUrl\n" +
                "                height\n" +
                "                width\n" +
                "                format\n" +
                "                hasWatermark\n" +
                "                }\n" +
                "                caption\n" +
                "                webShareUrl\n" +
                "                width\n" +
                "                height\n" +
                "                sharesCount\n" +
                "                viewsCount\n" +
                "                thumbnailUrl\n" +
                "                revealType\n" +
                "                hashtags\n" +
                "               \tcallToAction {\n" +
                "                  trackUrl\n" +
                "                  url\n" +
                "                  type\n" +
                "                  typeTranslation\n" +
                "                }\n" +
                "                \n" +
                "              \t\n" +
                "                  id\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "      ... on Error{ \n" +
                "       message\n" +
                "       }\n" +
                "     }\n" +
                "    }"

        private fun nextPageDiscoveryFeed(
            pageSize: Int = 2,
            feedId: String?,
            nextCursorId: String?
        ) = "{\n" +
                "  feed(id: \"$feedId\")\n" +
                "  {\n" +
                "    ... on Feed { \n" +
                "      id\n" +
                "      itemsConnection(first: $pageSize, after: \"$nextCursorId\") {\n" +
                "        pageInfo {\n" +
                "          startCursor\n" +
                "          endCursor\n" +
                "          hasPreviousPage\n" +
                "          hasNextPage\n" +
                "      \t}\n" +
                "        edges {\n" +
                "          cursor\n" +
                "          variant\n" +
                "          node {\n" +
                "            ... on Video {\n" +
                "            badge\n" +
                "            engagementUrl\n" +
                "            id\n" +
                "            videoType\n" +
                "            videoPosters {\n" +
                "              id\n" +
                "              format\n" +
                "              url\n" +
                "            }\n" +
                "            creator {\n" +
                "              id\n" +
                "              name\n" +
                "              username\n" +
                "              avatarUrl\n" +
                "            }\n" +
                "            products {\n" +
                "              id\n" +
                "              name\n" +
                "              currency\n" +
                "              options\n" +
                "              description\n" +
                "              images {\n" +
                "              id\n" +
                "              position\n" +
                "              url\n" +
                "              }\n" +
                "              units {\n" +
                "              id\n" +
                "              name\n" +
                "              position\n" +
                "              price\n" +
                "              url\n" +
                "              }\n" +
                "            }\n" +
                "            videoFiles {\n" +
                "            fileUrl\n" +
                "            height\n" +
                "            width\n" +
                "            format\n" +
                "            hasWatermark\n" +
                "            }\n" +
                "            caption\n" +
                "            webShareUrl\n" +
                "            width\n" +
                "            height\n" +
                "            sharesCount\n" +
                "            viewsCount\n" +
                "            thumbnailUrl\n" +
                "            revealType\n" +
                "            hashtags\n" +
                "           \tcallToAction {\n" +
                "              trackUrl\n" +
                "              url\n" +
                "              type\n" +
                "              typeTranslation\n" +
                "            }\n" +
                "            \n" +
                "          \t\n" +
                "              id\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "    }\n" +
                "    ... on FeedNotFoundError {\n" +
                "      message\n" +
                "    }\n" +
                "  ... on Error{ \n " +
                "  message\n" +
                "   }\n" +
                "  }\n" +
                "}"
    }
}