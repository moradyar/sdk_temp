package com.moradyar.authenticationfeature.core

import com.moradyar.authenticationfeature.impl.AuthenticatorImpl
import com.moradyar.networkcore.core.HttpClient
import com.moradyar.utilitycore.core.UniqueIdProvider
import com.moradyar.utilitycore.core.UserAgentInfoHelper

interface Authenticator {

    fun getRefreshedToken(onTokenReady: (token: Token) -> Unit)

    object Factory {
        fun getInstance(
            httpClient: HttpClient,
            clientUserId: String,
            guestUserId: String,
            userAgentInfoHelper: UserAgentInfoHelper,
            uniqueIdProvider: UniqueIdProvider
        ): Authenticator {
            return AuthenticatorImpl(
                httpClient,
                clientUserId,
                guestUserId,
                userAgentInfoHelper,
                uniqueIdProvider
            )
        }
    }
}