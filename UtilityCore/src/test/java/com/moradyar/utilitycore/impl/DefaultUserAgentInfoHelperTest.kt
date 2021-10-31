package com.moradyar.utilitycore.impl

import com.moradyar.utilitycore.core.UserAgentInfoHelper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class DefaultUserAgentInfoHelperTest {

    @RelaxedMockK
    private lateinit var environmentInfoProvider: EnvironmentInfoProvider

    private lateinit var classToTest: UserAgentInfoHelper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { environmentInfoProvider.getPackageName() } returns "test"
        every { environmentInfoProvider.getAndroidVersion() } returns "test"
        every { environmentInfoProvider.getAppInfo() } returns "test"
        every { environmentInfoProvider.getApplicationName() } returns "test"
        every { environmentInfoProvider.getDeviceCarrier() } returns "test"
        every { environmentInfoProvider.getLanguage() } returns "test"
        every { environmentInfoProvider.getOS() } returns "test"
        classToTest = DefaultUserAgentInfoHelper(environmentInfoProvider)
    }

    @Test
    fun `getUserAgentInfo should not return a empty string`() {
        val result = classToTest.getUserAgentInfo()

        assert(result.isNotEmpty())
    }

    @Test
    fun `getUserAgentInfo should always return a single response`() {
        val result = classToTest.getUserAgentInfo()
        val result2 = classToTest.getUserAgentInfo()

        assertEquals(result, result2)
    }
}