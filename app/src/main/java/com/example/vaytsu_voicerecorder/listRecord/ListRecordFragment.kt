package com.example.vaytsu_voicerecorder.listRecord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
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

        listRecordViewModel.records.observe(viewLifecycleOwner, {
            it?.let {
                if (it.isEmpty()) {
                    binding.emptyCartTextView.visibility = View.VISIBLE
                    binding.recordsTitle.visibility = View.GONE
                    binding.searchView.visibility = View.GONE
                } else {
                    binding.emptyCartTextView.visibility = View.GONE
                    binding.recordsTitle.visibility = View.VISIBLE
                    binding.searchView.visibility = View.VISIBLE
                }

                adapter.data = it
                adapter.dataFullCopy = it

                if (!binding.searchView.isIconified) {
                    binding.searchView.setQuery("", false)
                    binding.searchView.clearFocus()
                    binding.searchView.isIconified = true;
                }

            }
        })

        binding.lifecycleOwner = this

        binding.searchView.queryHint = "Введите название записи"
        binding.searchView.setOnSearchClickListener {
            binding.recordsTitle.visibility = View.GONE
        }
        binding.searchView.setOnCloseListener {
            binding.recordsTitle.visibility = View.VISIBLE
            false
        }
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        return binding.root
    }

}