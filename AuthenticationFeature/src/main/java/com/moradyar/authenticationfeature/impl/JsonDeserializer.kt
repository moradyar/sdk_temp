package com.moradyar.authenticationfeature.impl

import android.util.Base64
import android.util.Log
import org.json.JSONObject
import java.nio.charset.Charset

internal object JsonDeserializer {

    fun deserialize(jsonString: String): TokenResult {
        val jsonObject = JSONObject(jsonString)
        val expiresIn = ((jsonObject.optInt(TOKEN_EXPIRES_IN, 1800)) - 60) * 1000

        val refreshToken = jsonObject.optString(REFRESH_TOKEN, "")
        val accessToken = jsonObject.optString(ACCESS_TOKEN, "")
        val refreshTokenExpiredIn = jsonObject.optInt(REFRESH_TOKEN_EXPIRES_IN, 5184000)

        val idToken = jsonObject.optString(ID_TOKEN, "")

        if (!idToken.isNullOrBlank()) {
            try {
                val split =
                    idToken.split(SPLIT_REGEX.toRegex()).toTypedArray()
                Log.d(JWT_ENCODED, "${JWT_HEADER}${getJson(split[0])}")

                val bodyJson = JSONObject(getJson(split[1]))
                val userId = bodyJson.getString(USER_ID)
                val encodedClientId = bodyJson.getString(OAID)
                return TokenResult(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    refreshTokenExpiresInByMillis = refreshTokenExpiredIn,
                    idToken = idToken,
                    accessTokenExpiresInByMillis = expiresIn,
                    userId = userId,
                    encodedClientId = encodedClientId
                )
            } catch (e: Exception) {
                e.printStackTrace()
                return TokenResult(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    refreshTokenExpiresInByMillis = refreshTokenExpiredIn,
                    idToken = idToken,
                    accessTokenExpiresInByMillis = expiresIn
                )
            }
        }

        return TokenResult(
            accessToken = accessToken,
            refreshToken = refreshToken,
            refreshTokenExpiresInByMillis = refreshTokenExpiredIn,
            idToken = idToken,
            accessTokenExpiresInByMillis = expiresIn
        )
    }

    private fun getJson(strEncoded: String): String {
        val decodedBytes = Base64.decode(strEncoded, 0)
        return String(decodedBytes, Charset.forName(UTF_8_CHARSET))
    }

    // JWT decoding
    private const val JWT_ENCODED = "JWT_DECODED"
    private const val JWT_HEADER = "Header: "

    // Encoding
    private const val UTF_8_CHARSET = "UTF-8"

    // Json keys
    private const val USER_ID = "user_id"
    private const val OAID = "oaid"
    private const val SPLIT_REGEX = "\\."
    private const val ID_TOKEN = "id_token"
    private const val TOKEN_EXPIRES_IN = "token_expires_in"
    private const val REFRESH_TOKEN = "refresh_token"
    private const val ACCESS_TOKEN = "access_token"
    private const val REFRESH_TOKEN_EXPIRES_IN = "refresh_token_expires_in"
}