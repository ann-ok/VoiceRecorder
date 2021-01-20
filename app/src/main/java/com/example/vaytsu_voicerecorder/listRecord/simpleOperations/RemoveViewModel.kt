package com.example.vaytsu_voicerecorder.listRecord.simpleOperations

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.vaytsu_voicerecorder.R
import com.example.vaytsu_voicerecorder.database.RecordDatabase
import com.example.vaytsu_voicerecorder.database.RecordDatabaseDao
import kotlinx.coroutines.*
import java.io.File
import java.lang.Exception

class RemoveViewModel (
    private var databaseDao: RecordDatabaseDao,
    private val application: Application
) : ViewModel() {

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun removeItem(itemId: Long) {
        databaseDao = RecordDatabase.getInstance(application).recordDatabaseDao

        try {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    databaseDao.removeRecord(itemId)
                }
            }
        } catch (e: Exception) {
            Log.e("removeItem", "exception", e)
        }
    }

    fun removeFile(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
            Toast.makeText(application, R.string.file_deleted_text, Toast.LENGTH_SHORT).show()
        }
    }

}