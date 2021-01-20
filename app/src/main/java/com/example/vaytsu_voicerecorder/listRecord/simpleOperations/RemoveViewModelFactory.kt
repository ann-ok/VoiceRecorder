package com.example.vaytsu_voicerecorder.listRecord.simpleOperations

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vaytsu_voicerecorder.database.RecordDatabaseDao
import java.lang.IllegalArgumentException

class RemoveViewModelFactory (
    private val databaseDao: RecordDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoveViewModel::class.java)) {
            return RemoveViewModel(
                databaseDao,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}