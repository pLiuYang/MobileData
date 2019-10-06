package com.assignment.mobiledata.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.assignment.mobiledata.data.IMobileDataRepository
import com.assignment.mobiledata.data.Record
import com.assignment.mobiledata.data.RecordDisplayModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class RecordUseCasesTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mobileDataRepository: IMobileDataRepository

    private lateinit var recordUseCases: RecordUseCases

    private lateinit var lifecycleOwner: LifecycleOwner

    private val recordsLiveData = MutableLiveData<List<Record>>()

    private val errorLiveData = MutableLiveData<String>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        `when`(mobileDataRepository.getRecordsLiveData()).thenReturn(recordsLiveData)
        `when`(mobileDataRepository.getErrorLiveData()).thenReturn(errorLiveData)
        recordUseCases = RecordUseCases(mobileDataRepository)

        lifecycleOwner = object : LifecycleOwner {
            private val mLifecycle = init()

            private fun init(): LifecycleRegistry {
                val registry = LifecycleRegistry(this)
                registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
                registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
                registry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
                return registry
            }

            override fun getLifecycle(): Lifecycle {
                return mLifecycle
            }
        }
    }

    @Test
    fun testLoadMobileData() {
        // When
        recordUseCases.loadMobileData()

        // Then
        verify(mobileDataRepository).loadMobileData()
    }

    @Test
    fun testRecordsLiveData() {
        // Given
        val observer = mock(Observer::class.java) as Observer<List<RecordDisplayModel>>
        recordUseCases.getYearlyRecordsLiveData().observe(lifecycleOwner, observer)

        // When
        recordsLiveData.value = listOf()

        // Then
        verify(observer).onChanged(any())
    }

    @Test
    fun testErrorLiveData() {
        // Given
        val observer = mock(Observer::class.java) as Observer<String>
        recordUseCases.getErrorLiveData().observe(lifecycleOwner, observer)

        // When
        errorLiveData.value = "error"

        // Then
        verify(observer).onChanged(any())
    }

    @Test
    fun testRecordsToDisplayModels() {
        // Given
        val records = listOf(
            Record("0.1", "2004-Q1", 1),
            Record("0.1", "2007-Q1", 2),
            Record("0.1", "2008-Q1", 3),
            Record("0.1", "2011-Q1", 4),
            Record("0.2", "2012-Q1", 5),
            Record("0.1", "2012-Q2", 6),
            Record("0.1", "2012-Q4", 7),
            Record("0.3", "2017-Q4", 8),
            Record("0.1", "2018-Q1", 9),
            Record("0.1", "2018-Q2", 10),
            Record("0.1", "2019-Q1", 11)
        )

        // When
        val recordDisplayModels = recordUseCases.recordsToDisplayModels(records)

        // Then
        assertEquals(5, recordDisplayModels.size)

        assertEquals("2008", recordDisplayModels[0].year)
        assertEquals("0.1", recordDisplayModels[0].dataVolume)
        assertEquals(false, recordDisplayModels[0].downTrending)

        assertEquals("2011", recordDisplayModels[1].year)
        assertEquals("0.1", recordDisplayModels[1].dataVolume)
        assertEquals(false, recordDisplayModels[1].downTrending)

        assertEquals("2012", recordDisplayModels[2].year)
        assertEquals("0.4", recordDisplayModels[2].dataVolume)
        assertEquals(true, recordDisplayModels[2].downTrending)

        assertEquals("2017", recordDisplayModels[3].year)
        assertEquals("0.3", recordDisplayModels[3].dataVolume)
        assertEquals(false, recordDisplayModels[3].downTrending)

        assertEquals("2018", recordDisplayModels[4].year)
        assertEquals("0.2", recordDisplayModels[4].dataVolume)
        assertEquals(true, recordDisplayModels[4].downTrending)
    }
}
