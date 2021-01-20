package com.example.vaytsu_voicerecorder.listRecord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.vaytsu_voicerecorder.R
import com.example.vaytsu_voicerecorder.database.RecordDatabase
import com.example.vaytsu_voicerecorder.databinding.FragmentListRecordBinding

class ListRecordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentListRecordBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list_record, container, false
        )

        val application = requireNotNull(this.activity).application

        val dataSource =  RecordDatabase.getInstance(application).recordDatabaseDao
        val viewModelFactory = ListRecordViewModelFactory(dataSource)

        val listRecordViewModel = ViewModelProvider(this, viewModelFactory)
                .get(ListRecordViewModel::class.java)

        binding.listRecordViewModel = listRecordViewModel

        val adapter = ListRecordAdapter()

        binding.recyclerView.adapter = adapter

        listRecordViewModel.records.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.emptyCartTextView.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                adapter.data = it
            }
        })

        binding.lifecycleOwner = this

        return binding.root
    }
}