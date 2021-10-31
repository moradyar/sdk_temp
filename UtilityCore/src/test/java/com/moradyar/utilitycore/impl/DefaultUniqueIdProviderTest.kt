package com.moradyar.utilitycore.impl

import org.junit.Assert.assertEquals
import org.junit.Test

internal class DefaultUniqueIdProviderTest {

    private val classToTest = DefaultUniqueIdProvider()

    @Test
    fun `getUUID should return a non-empty string`() {
        val uuid = classToTest.getUUID()

        assert(uuid.isNotEmpty())
    }

    @Test
    fun `getUUID should return a string with size of 36`() {
        val uuid = classToTest.getUUID()

        assertEquals(36, uuid.length)
    }
}