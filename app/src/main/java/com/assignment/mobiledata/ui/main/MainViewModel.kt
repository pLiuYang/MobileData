package com.assignment.mobiledata.ui.main

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.assignment.mobiledata.data.IMobileDataRepository

class MainViewModel(private val mobileDataRepo: IMobileDataRepository) : ViewModel() {

    val dataList = Transformations.map(mobileDataRepo.getRecordsLiveData()) { it }

    val error = Transformations.map(mobileDataRepo.getErrorLiveData()) { it }

    fun loadData() = mobileDataRepo.loadMobileData()

}
