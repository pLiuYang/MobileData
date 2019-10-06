package com.assignment.mobiledata.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _dataList = MutableLiveData<List<String>>()
    val dataList: LiveData<List<String>> = _dataList

    fun loadData() {
        _dataList.value = listOf("0.5", "8.8", "1.2")
    }

}
