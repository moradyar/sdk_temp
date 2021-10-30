package com.moradyar.authenticationfeature.impl

internal data class TokenResult(
    val accessToken: String,
    val refreshToken: String,
    private val refreshTokenExpiresInByMillis: Int,
    private val accessTokenExpiresInByMillis: Int,
    val idToken: String,
    val userId: String = "",
    val encodedClientId: String = ""
) {

    var accessTokenExpireTimeStamp: Long = 0
    var refreshTokenExpireTimeStamp: Long = 0

    init {
        accessTokenExpireTimeStamp = System.currentTimeMillis() + accessTokenExpiresInByMillis
        refreshTokenExpireTimeStamp = System.currentTimeMillis() + refreshTokenExpiresInByMillis
    }
}
