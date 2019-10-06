package com.assignment.mobiledata.domain

import androidx.lifecycle.LiveData
import com.assignment.mobiledata.data.RecordDisplayModel

interface IRecordUseCases {

    fun getYearlyRecordsLiveData(): LiveData<List<RecordDisplayModel>>

    fun getErrorLiveData(): LiveData<String>

    fun loadMobileData()

}
