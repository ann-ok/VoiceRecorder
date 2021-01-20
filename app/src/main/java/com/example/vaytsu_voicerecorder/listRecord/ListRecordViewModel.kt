package com.example.vaytsu_voicerecorder.listRecord

import androidx.lifecycle.ViewModel
import com.example.vaytsu_voicerecorder.database.RecordDatabaseDao

class ListRecordViewModel (
        dataSource: RecordDatabaseDao
) : ViewModel() {

    val database = dataSource
    val records = database.getAllRecords()
}