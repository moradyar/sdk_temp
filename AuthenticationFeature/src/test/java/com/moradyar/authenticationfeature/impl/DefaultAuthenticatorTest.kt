package com.moradyar.authenticationfeature.impl

import com.moradyar.authenticationfeature.core.Authenticator
import com.moradyar.authenticationfeature.core.Token
import com.moradyar.networkcore.core.HttpClient
import com.moradyar.networkcore.core.RequestState
import com.moradyar.utilitycore.core.UniqueIdProvider
import com.moradyar.utilitycore.core.UserAgentInfoHelper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class DefaultAuthenticatorTest {

    @MockK
    private lateinit var httpClient: HttpClient

    @MockK
    private lateinit var userAgentInfoHelper: UserAgentInfoHelper

    @MockK
    private lateinit var uniqueIdProvider: UniqueIdProvider

    private lateinit var classToTest: Authenticator

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(JsonDeserializer)
        every { userAgentInfoHelper.getUserAgentInfo() } returns "test"
        every { uniqueIdProvider.getUUID() } returns "test"
        classToTest = DefaultAuthenticator(
            httpClient = httpClient,
            clientId = "",
            guestId = "",
            userAgentInfoHelper = userAgentInfoHelper,
            uniqueIdProvider = uniqueIdProvider
        )
    }

    @Test
    fun `getRefreshedToken should call callback when it is the first time and success`() {
        val callback: (token: Token) -> Unit = mockk(relaxed = true)
        every { JsonDeserializer.deserialize(any()) } returns getTestTokenResult()
        every { httpClient.fetchRaw(any(), any()) } answers {
            val onResponseReady = args[1] as (response: RequestState) -> Unit
            onResponseReady(RequestState.Success(""))
        }

        classToTest.getRefreshedToken(callback)

        val tokenSlot = slot<Token>()

        verify { callback.invoke(capture(tokenSlot)) }

        assertEquals("bearer: test", tokenSlot.captured.authToken)
        assertEquals("test", tokenSlot.captured.sessionId)
        assertEquals("test", tokenSlot.captured.userAgent)
    }

    @Test
    fun `getRefreshedToken should call callback when it is the first time and failed`() {
        val callback: (token: Token) -> Unit = mockk(relaxed = true)
        every { JsonDeserializer.deserialize(any()) } returns getTestTokenResult()
        every { httpClient.fetchRaw(any(), any()) } answers {
            val onResponseReady = args[1] as (response: RequestState) -> Unit
            onResponseReady(RequestState.Error(IllegalStateException()))
        }

        classToTest.getRefreshedToken(callback)

        val tokenSlot = slot<Token>()

        verify { callback.invoke(capture(tokenSlot)) }

        assertEquals("", tokenSlot.captured.authToken)
        assertEquals("", tokenSlot.captured.sessionId)
        assertEquals("", tokenSlot.captured.userAgent)
    }

    @Test
    fun `getRefreshedToken should call callback when the accessToken is still valid`() {
        val callback: (token: Token) -> Unit = mockk(relaxed = true)
        every { JsonDeserializer.deserialize(any()) } returns getTestTokenResult(
            accessTokenExpiresInByMillis = 5000
        )
        every { httpClient.fetchRaw(any(), any()) } answers {
            val onResponseReady = args[1] as (response: RequestState) -> Unit
            onResponseReady(RequestState.Success(""))
        }

        classToTest.getRefreshedToken(callback)

        val tokenSlot1 = slot<Token>()
        verify { callback.invoke(capture(tokenSlot1)) }
        assertEquals("bearer: test", tokenSlot1.captured.authToken)
        assertEquals("test", tokenSlot1.captured.sessionId)
        assertEquals("test", tokenSlot1.captured.userAgent)

        val callback2: (token: Token) -> Unit = mockk(relaxed = true)
        every { JsonDeserializer.deserialize(any()) } returns getTestTokenResult(
            accessTokenExpiresInByMillis = 0,
            accessToken = "test2"
        )
        classToTest.getRefreshedToken(callback2)

        val tokenSlot2 = slot<Token>()

        verify { callback2.invoke(capture(tokenSlot2)) }

        assertEquals("bearer: test", tokenSlot2.captured.authToken)
        assertEquals("test", tokenSlot2.captured.sessionId)
        assertEquals("test", tokenSlot2.captured.userAgent)
    }

    @Test
    fun `getRefreshedToken should call callback when the refreshToken is still valid`() {
        val callback: (token: Token) -> Unit = mockk(relaxed = true)
        every { JsonDeserializer.deserialize(any()) } returns getTestTokenResult(
            accessTokenExpiresInByMillis = 0,
            refreshTokenExpiresInByMillis = 5000
        )
        every { httpClient.fetchRaw(any(), any()) } answers {
            val onResponseReady = args[1] as (response: RequestState) -> Unit
            onResponseReady(RequestState.Success(""))
        }

        classToTest.getRefreshedToken(callback)

        val tokenSlot1 = slot<Token>()
        verify { callback.invoke(capture(tokenSlot1)) }
        assertEquals("bearer: test", tokenSlot1.captured.authToken)
        assertEquals("test", tokenSlot1.captured.sessionId)
        assertEquals("test", tokenSlot1.captured.userAgent)

        val callback2: (token: Token) -> Unit = mockk(relaxed = true)
        every { JsonDeserializer.deserialize(any()) } returns getTestTokenResult(
            accessTokenExpiresInByMillis = 0,
            accessToken = "test3"
        )
        classToTest.getRefreshedToken(callback2)

        val tokenSlot2 = slot<Token>()

        verify { callback2.invoke(capture(tokenSlot2)) }

        assertEquals("bearer: test3", tokenSlot2.captured.authToken)
        assertEquals("test", tokenSlot2.captured.sessionId)
        assertEquals("test", tokenSlot2.captured.userAgent)
    }

    private fun getTestTokenResult(
        accessTokenExpiresInByMillis: Int = 0,
        refreshTokenExpiresInByMillis: Int = 0,
        accessToken: String = "test"
    ): TokenResult {
        return TokenResult(
            accessToken = accessToken,
            refreshToken = "test",
            refreshTokenExpiresInByMillis = refreshTokenExpiresInByMillis,
            accessTokenExpiresInByMillis = accessTokenExpiresInByMillis,
            idToken = "test",
            userId = "test",
            encodedClientId = "test"
        )
    }
}