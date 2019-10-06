package com.assignment.mobiledata.data

import androidx.lifecycle.LiveData

interface IMobileDataRepository {

    fun loadMobileData()
    fun getRecordsLiveData(): LiveData<List<Record>>
    fun getErrorLiveData(): LiveData<String>

}
