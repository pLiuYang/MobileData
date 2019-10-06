package com.assignment.mobiledata.data

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.assignment.mobiledata.data.local.RecordDao
import com.assignment.mobiledata.util.readStream
import org.json.JSONException
import org.json.JSONObject
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL

class MobileDataRepository(private val recordDao: RecordDao) : IMobileDataRepository {

    private val records = MutableLiveData<List<Record>>()
    private val error = MutableLiveData<String>()

    override fun loadMobileData() {
        Thread(Runnable {
            if (apiAvailable(ENDPOINT)) {
                callApi(ENDPOINT)
            } else {
                loadCache()
            }
        }).start()
    }

    override fun getRecordsLiveData(): LiveData<List<Record>> = records

    override fun getErrorLiveData(): LiveData<String> = error

    @VisibleForTesting
    fun callApi(endpoint: String) {
        try {
            val url = URL(endpoint)
            val con = url.openConnection() as HttpURLConnection

            if (con.responseCode == HttpURLConnection.HTTP_OK) {
                val readStream = con.inputStream.readStream()
                onSuccess(readStream)
            } else {
                onFailure(con.responseCode, con.responseMessage)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onSuccess(data: String) {
        try {
            JSONObject(data).optJSONObject("result")
                ?.optJSONArray("records")
                ?.let { array ->
                    val list = mutableListOf<Record>()
                    for (i in 0 until array.length()) {
                        list.add(Record(array.optJSONObject(i)))
                    }
                    records.postValue(list)
                    recordDao.deleteAllRecords()
                    recordDao.insertAllRecords(list)
                }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    @VisibleForTesting
    fun apiAvailable(endpoint: String): Boolean {
        var available = false
        try {
            available = URL(endpoint).openConnection().getInputStream().readStream().isNotEmpty()
        } catch (e: ConnectException) {
            e.printStackTrace()
        }
        return available
    }

    @VisibleForTesting
    fun loadCache() {
        val list = recordDao.getAllRecords()
        if (list.isEmpty()) {
            error.postValue("No records found")
        } else {
            records.postValue(list)
        }
    }

    private fun onFailure(responseCode: Int, responseMessage: String) {
        error.postValue("Response code: $responseCode, message: $responseMessage")
    }

    companion object {
        private const val ENDPOINT =
            "https://data.gov.sg/api/action/datastore_search?resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f"
    }

}
