package com.moradyar.authenticationfeature.impl

import com.moradyar.authenticationfeature.core.Authenticator
import com.moradyar.authenticationfeature.core.Token
import com.moradyar.networkcore.core.HttpClient
import com.moradyar.networkcore.core.HttpRequest
import com.moradyar.networkcore.core.RequestState
import com.moradyar.utilitycore.core.UniqueIdProvider
import com.moradyar.utilitycore.core.UserAgentInfoHelper

internal class DefaultAuthenticator(
    private val httpClient: HttpClient,
    private val clientId: String,
    private val guestId: String,
    private val userAgentInfoHelper: UserAgentInfoHelper,
    private val uniqueIdProvider: UniqueIdProvider
) : Authenticator {

    private var latestTokenResult: TokenResult? = null

    override fun getRefreshedToken(onTokenReady: (token: Token) -> Unit) {
        if (latestTokenResult == null) {
            getToken(onTokenReady = onTokenReady)
        } else {
            latestTokenResult?.refreshToken?.let { refToken ->
                when {
                    currentTime() < latestTokenResult?.accessTokenExpireTimeStamp ?: 0L -> {
                        onTokenReady(makeTokenFromLatestTokenResult())
                    }
                    currentTime() < latestTokenResult?.refreshTokenExpireTimeStamp ?: 0L -> {
                        getToken(refToken, onTokenReady)
                    }
                    else -> {
                        getToken(onTokenReady = onTokenReady)
                    }
                }
            }
        }
    }

    private fun makeTokenFromLatestTokenResult(): Token {
        return Token(
            authToken = "$BEARER${latestTokenResult?.accessToken}",
            userAgent = userAgentInfoHelper.getUserAgentInfo(),
            sessionId = uniqueIdProvider.getUUID()
        )
    }

    private fun currentTime(): Long {
        return System.currentTimeMillis()
    }

    private fun getToken(refreshToken: String? = null, onTokenReady: (token: Token) -> Unit) {
        val userAgentInfo = userAgentInfoHelper.getUserAgentInfo()
        val req = HttpRequest
            .Builder(END_POINT)
            .headers(createHeaders(userAgentInfo))
            .queryParameters(createParameters(refreshToken))
            .useCache(false)
            .method(HttpRequest.Method.GET)
            .build()
        httpClient.fetchRaw(req) { state ->
            when (state) {
                is RequestState.Error -> onTokenReady(Token.getInvalidToken())
                is RequestState.Success -> {
                    latestTokenResult = JsonDeserializer.deserialize(state.value)
                    onTokenReady(makeTokenFromLatestTokenResult())
                }
            }
        }
    }

    private fun createParameters(
        refreshToken: String? = null
    ): Map<String, String> {
        return if (refreshToken == null) {
            mapOf(
                GRANT_TYPE_PARAM to GRANT_TYPE_GUEST_ID,
                SCOPE_PARAM to SCOPE_VALUE,
                CLIENT_ID_PARAM to clientId,
                GUEST_U_ID_PARAM to guestId
            )
        } else {
            mapOf(
                GRANT_TYPE_PARAM to GRANT_TYPE_REFRESH_TOKEN,
                SCOPE_PARAM to SCOPE_VALUE,
                CLIENT_ID_PARAM to clientId,
                GUEST_U_ID_PARAM to guestId,
                REDIRECT_URI_PARAM to REDIRECT_URI_VALUE,
                REFRESH_TOKEN_PARAM to refreshToken
            )
        }
    }

    private fun createHeaders(userAgentInfo: String): Map<String, String> {
        return mapOf(
            ACCEPT_HEADER to ACCEPT_HEADER_VALUE,
            USER_AGENT_HEADER to userAgentInfo
        )
    }

    companion object {
        // General
        private const val END_POINT = "/oauth/token"
        private const val BEARER = "bearer: "

        // Parameters
        private const val GRANT_TYPE_PARAM = "grant_type"
        private const val GRANT_TYPE_GUEST_ID = "guest_uid"
        private const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
        private const val SCOPE_PARAM = "scope"
        private const val SCOPE_VALUE = "openid"
        private const val CLIENT_ID_PARAM = "client_id"
        private const val GUEST_U_ID_PARAM = "guest_uid"
        private const val REDIRECT_URI_PARAM = "redirect_uri"
        private const val REDIRECT_URI_VALUE = "REDIRECT_URI"
        private const val REFRESH_TOKEN_PARAM = "refresh_token"

        // Headers
        private const val ACCEPT_HEADER = "Accept"
        private const val ACCEPT_HEADER_VALUE = "application/json"
        private const val USER_AGENT_HEADER = "User-Agent"
    }
}