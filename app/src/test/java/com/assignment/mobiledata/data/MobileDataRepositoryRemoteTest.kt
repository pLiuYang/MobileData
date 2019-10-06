package com.assignment.mobiledata.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.verify
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLStreamHandlerFactory

class MobileDataRepositoryRemoteTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var lifecycleOwner: LifecycleOwner

    private val mobileDataRepositoryRemote = MobileDataRepositoryRemote()

    private lateinit var recordsObserver: Observer<List<Record>>
    private lateinit var errorObserver: Observer<String>

    @Before
    fun setUp() {
        httpUrlStreamHandler.resetConnections()

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

        recordsObserver = Mockito.mock(Observer::class.java) as Observer<List<Record>>
        mobileDataRepositoryRemote.getRecordsLiveData().observe(lifecycleOwner, recordsObserver)

        errorObserver = Mockito.mock(Observer::class.java) as Observer<String>
        mobileDataRepositoryRemote.getErrorLiveData().observe(lifecycleOwner, errorObserver)
    }

    @Test
    fun testCallApi_success() {
        val urlConnection = Mockito.mock(HttpURLConnection::class.java)
        httpUrlStreamHandler.addConnection(URL(ENDPOINT), urlConnection)
        given(urlConnection.responseCode).willReturn(200)
        given(urlConnection.inputStream).willReturn(API_RESPONSE.byteInputStream())

        // When
        mobileDataRepositoryRemote.callApi(ENDPOINT)

        // Then
        verify(recordsObserver).onChanged(any())
    }

    @Test
    fun testCallApi_failure_400() {
        val urlConnection = Mockito.mock(HttpURLConnection::class.java)
        httpUrlStreamHandler.addConnection(URL(ENDPOINT), urlConnection)
        given(urlConnection.responseCode).willReturn(400)
        given(urlConnection.responseMessage).willReturn("Something wrong")

        // When
        mobileDataRepositoryRemote.callApi(ENDPOINT)

        // Then
        verify(errorObserver).onChanged(any())
    }

    @Test
    fun testCallApi_failure_500() {
        val urlConnection = Mockito.mock(HttpURLConnection::class.java)
        httpUrlStreamHandler.addConnection(URL(ENDPOINT), urlConnection)
        given(urlConnection.responseCode).willReturn(500)
        given(urlConnection.responseMessage).willReturn("Something wrong")

        // When
        mobileDataRepositoryRemote.callApi(ENDPOINT)

        // Then
        verify(errorObserver).onChanged(any())
    }

    companion object {

        private const val ENDPOINT = "test://test"
        private const val API_RESPONSE = "{\"help\": \"https://data.gov.sg/api/3/action/help_show?name=datastore_search\", \"success\": true, \"result\": {\"resource_id\": \"a807b7ab-6cad-4aa6-87d0-e283a7353a0f\", \"fields\": [{\"type\": \"int4\", \"id\": \"_id\"}, {\"type\": \"text\", \"id\": \"quarter\"}, {\"type\": \"numeric\", \"id\": \"volume_of_mobile_data\"}], \"records\": [{\"volume_of_mobile_data\": \"0.000384\", \"quarter\": \"2004-Q3\", \"_id\": 1}, {\"volume_of_mobile_data\": \"0.000543\", \"quarter\": \"2004-Q4\", \"_id\": 2}, {\"volume_of_mobile_data\": \"0.00062\", \"quarter\": \"2005-Q1\", \"_id\": 3}, {\"volume_of_mobile_data\": \"0.000634\", \"quarter\": \"2005-Q2\", \"_id\": 4}, {\"volume_of_mobile_data\": \"0.000718\", \"quarter\": \"2005-Q3\", \"_id\": 5}], \"_links\": {\"start\": \"/api/action/datastore_search?limit=5&resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f\", \"next\": \"/api/action/datastore_search?offset=5&limit=5&resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f\"}, \"limit\": 5, \"total\": 59}}"

        private lateinit var httpUrlStreamHandler: HttpUrlStreamHandler
        private lateinit var urlStreamHandlerFactory: URLStreamHandlerFactory

        @BeforeClass
        @JvmStatic
        fun setUpURLStreamHandlerFactory() {
            // Allows for mocking URL connections
            urlStreamHandlerFactory = Mockito.mock(URLStreamHandlerFactory::class.java)
            URL.setURLStreamHandlerFactory(urlStreamHandlerFactory)

            httpUrlStreamHandler = HttpUrlStreamHandler()
            BDDMockito.given(urlStreamHandlerFactory.createURLStreamHandler("test")).willReturn(
                httpUrlStreamHandler
            )
        }
    }
}
