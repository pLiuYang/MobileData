package com.assignment.mobiledata.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.assignment.mobiledata.data.MobileDataRepositoryRemote
import com.assignment.mobiledata.ui.main.MainViewModel

class ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MobileDataRepositoryRemote()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
