package com.assignment.mobiledata.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.assignment.mobiledata.data.MobileDataRepository
import com.assignment.mobiledata.data.local.RecordDatabase
import com.assignment.mobiledata.domain.RecordUseCases
import com.assignment.mobiledata.ui.main.MainViewModel

class ViewModelFactory(private val appContext: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            // TODO use Dependency Injection to simplify the code
            return MainViewModel(
                RecordUseCases(
                    MobileDataRepository(
                        Room.databaseBuilder(
                            appContext,
                            RecordDatabase::class.java,
                            "records"
                        ).build().recordDao()
                    )
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
