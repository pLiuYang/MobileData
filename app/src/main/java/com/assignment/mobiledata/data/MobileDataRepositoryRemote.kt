package com.assignment.mobiledata.data

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.assignment.mobiledata.util.readStream
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MobileDataRepositoryRemote : IMobileDataRepository {

    private val records = MutableLiveData<List<Record>>()
    private val error = MutableLiveData<String>()

    override fun loadMobileData() {
        Thread(Runnable {
            callApi()
        }).start()
    }

    override fun getRecordsLiveData(): LiveData<List<Record>> = records

    override fun getErrorLiveData(): LiveData<String> = error

    @VisibleForTesting
    fun callApi() {
        try {
            val url = URL(ENDPOINT)
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
                }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun onFailure(responseCode: Int, responseMessage: String) {
        error.postValue("Response code: $responseCode, message: $responseMessage")
    }

    companion object {
        private const val ENDPOINT =
            "https://data.gov.sg/api/action/datastore_search?resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f&limit=5"
    }

}
