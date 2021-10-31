package com.moradyar.authenticationfeature.core

import com.moradyar.authenticationfeature.impl.DefaultAuthenticator
import com.moradyar.networkcore.core.HttpClient
import com.moradyar.utilitycore.core.UniqueIdProvider
import com.moradyar.utilitycore.core.UserAgentInfoHelper

interface Authenticator {

    fun getRefreshedToken(onTokenReady: (token: Token) -> Unit)

    object Factory {
        fun getInstance(
            httpClient: HttpClient,
            clientId: String,
            guestUserId: String,
            userAgentInfoHelper: UserAgentInfoHelper,
            uniqueIdProvider: UniqueIdProvider
        ): Authenticator {
            return DefaultAuthenticator(
                httpClient,
                clientId,
                guestUserId,
                userAgentInfoHelper,
                uniqueIdProvider
            )
        }
    }
}