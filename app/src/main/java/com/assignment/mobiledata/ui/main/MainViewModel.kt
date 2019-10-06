package com.assignment.mobiledata.ui.main

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.assignment.mobiledata.domain.IRecordUseCases

class MainViewModel(private val recordUseCases: IRecordUseCases) : ViewModel() {

    val dataList = Transformations.map(recordUseCases.getYearlyRecordsLiveData()) { it }

    val error = Transformations.map(recordUseCases.getErrorLiveData()) { it }

    fun loadData() = recordUseCases.loadMobileData()

}
