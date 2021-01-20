package com.example.vaytsu_voicerecorder.listRecord.simpleOperations

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.vaytsu_voicerecorder.R
import com.example.vaytsu_voicerecorder.database.RecordDatabase

class RemoveDialogFragment : DialogFragment() {

    private lateinit var viewModel: RemoveViewModel

    companion object {
        private const val ARG_ITEM_PATH = "recording_item_path"
        private const val ARG_ITEM_ID = "recording_item_id"
    }

    fun newInstance(itemId: Long, itemPath: String?) : RemoveDialogFragment {
        val f = RemoveDialogFragment()
        val b = Bundle()
        b.putLong(ARG_ITEM_ID, itemId)
        b.putString(ARG_ITEM_PATH, itemPath)

        f.arguments = b
        return f
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val application = requireNotNull(this.activity).application

        val dataSource = RecordDatabase.getInstance(application).recordDatabaseDao
        val viewModelFactory = RemoveViewModelFactory(
            dataSource,
            application
        )

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(RemoveViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val itemPath = arguments?.getString(ARG_ITEM_PATH)
        val itemId = arguments?.getLong(ARG_ITEM_ID)

        return AlertDialog.Builder(activity)
            .setTitle(R.string.dialog_title_delete)
            .setMessage(R.string.dialog_text_delete)
            .setPositiveButton(R.string.dialog_action_yes) {
                    dialog, _ ->
                try {
                    itemId?.let { viewModel.removeItem(it) }
                    itemPath?.let { viewModel.removeFile(it) }
                } catch (e: java.lang.Exception) {
                    Log.e("deleteFileDialog", "exception", e)
                }
                dialog.cancel()
            }
            .setNegativeButton(
                R.string.dialog_action_no
            ) { dialog, _ ->
                dialog.cancel()
            }
            .create()
    }

}