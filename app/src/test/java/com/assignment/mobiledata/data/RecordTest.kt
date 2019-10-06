package com.assignment.mobiledata.data

import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.doThrow

class RecordTest {

    @Test
    fun testConstructor() {
        // Given
        val jsonObject =
            JSONObject("{\"volume_of_mobile_data\":\"0.1\", \"quarter\":\"2013-Q1\", \"_id\":2}")

        // When
        val record = Record(jsonObject)

        // Then
        assertEquals("0.1", record.volumeOfMobileData)
        assertEquals("2013-Q1", record.quarter)
        assertEquals(2, record.id)
    }

    @Test
    fun testGetYearExtension() {
        // Given
        val record = Record("0.2", "2014-Q1", 1)

        // Then
        assertEquals("2014", record.getYear())
    }

    @Test(expected = StringIndexOutOfBoundsException::class)
    fun testGetYearExtension_exception() {
        // Given
        val record = Record("0.2", "201", 1)

        // Then
        record.getYear()
    }

    @Test
    fun testGetVolumeOfDataInFloatExtension() {
        // Given
        val record = Record("0.2", "2014-Q1", 1)

        // Then
        assertEquals(0.2f, record.getVolumeOfDataInFloat())
    }

    @Test
    fun testGetVolumeOfDataInFloatExtension_invalidInput() {
        // Given
        val record = Record("invalid", "2014-Q1", 1)

        // Then
        assertEquals(0f, record.getVolumeOfDataInFloat())
    }
}
