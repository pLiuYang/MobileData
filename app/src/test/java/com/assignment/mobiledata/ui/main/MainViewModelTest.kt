package com.assignment.mobiledata.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.assignment.mobiledata.data.RecordDisplayModel
import com.assignment.mobiledata.domain.IRecordUseCases
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var recordUseCases: IRecordUseCases

    private lateinit var mainViewModel: MainViewModel

    private lateinit var lifecycleOwner: LifecycleOwner

    private val recordDisplayModelsLiveData = MutableLiveData<List<RecordDisplayModel>>()

    private val errorLiveData = MutableLiveData<String>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(recordUseCases.getYearlyRecordsLiveData())
            .thenReturn(recordDisplayModelsLiveData)
        Mockito.`when`(recordUseCases.getErrorLiveData()).thenReturn(errorLiveData)
        mainViewModel = MainViewModel(recordUseCases)

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
    fun testLoadData() {
        // When
        mainViewModel.loadData()

        // Then
        Mockito.verify(recordUseCases).loadMobileData()
    }

    @Test
    fun testRecordsLiveData() {
        // Given
        val observer = Mockito.mock(Observer::class.java) as Observer<List<RecordDisplayModel>>
        mainViewModel.dataList.observe(lifecycleOwner, observer)

        // When
        recordDisplayModelsLiveData.value = listOf()

        // Then
        Mockito.verify(observer).onChanged(ArgumentMatchers.any())
    }

    @Test
    fun testErrorLiveData() {
        // Given
        val observer = Mockito.mock(Observer::class.java) as Observer<String>
        mainViewModel.error.observe(lifecycleOwner, observer)

        // When
        errorLiveData.value = "error"

        // Then
        Mockito.verify(observer).onChanged(ArgumentMatchers.any())
    }
}
