package com.example.vaytsu_voicerecorder.listRecord

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.vaytsu_voicerecorder.R
import com.example.vaytsu_voicerecorder.database.RecordingItem
import com.example.vaytsu_voicerecorder.listRecord.simpleOperations.RemoveDialogFragment
import com.example.vaytsu_voicerecorder.player.PlayerFragment
import kotlinx.android.synthetic.main.list_item_record.view.*
import java.io.File
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

class ListRecordAdapter: RecyclerView.Adapter<ListRecordAdapter.ViewHolder>(), Filterable {

    var data = listOf<RecordingItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var dataFullCopy = listOf<RecordingItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var vName: TextView = itemView.fileNameTextView
        var vLength: TextView = itemView.fileLengthTextView
        var cardView: View = itemView.cardView
        var deleteCardIV: ImageView = itemView.deleteCardIV
        //var editCardIV: ImageView = itemView.editCardIV

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view: View = layoutInflater.inflate(R.layout.list_item_record, parent, false)
                return ViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context: Context = holder.itemView.context

        val recordingItem = data[position]

        val itemDuration: Long = recordingItem.length

        val minutes = TimeUnit.MILLISECONDS.toMinutes((itemDuration))
        val seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration) - TimeUnit.MINUTES.toSeconds(minutes)

        holder.vName.text = recordingItem.name
        holder.vLength.text = String.format("%02d:%02d", minutes, seconds)

        holder.cardView.setOnClickListener {
            val filePath = recordingItem.filePath

            val file = File(filePath)
            if (file.exists()) {
                try {
                    playRecord(filePath, context)
                } catch (e: Exception) {
                    Log.e("ListRecordAdapter", "Ошибка воспроизведения аудиофайла")
                }
            } else {
                Toast.makeText(context, "Аудиофайл не найден", Toast.LENGTH_SHORT).show()
            }
        }

        holder.deleteCardIV.setOnClickListener {
            removeItemDialog(recordingItem, context)
        }

//        holder.editCardIV.setOnClickListener {
//            editItemDialog(recordingItem, context)
//        }
    }

    override fun getItemCount() = data.size

    private fun playRecord(filePath: String, context: Context) {
        val playerFragment = PlayerFragment().newInstance(filePath)
        val transaction: FragmentTransaction = (context as FragmentActivity)
            .supportFragmentManager
            .beginTransaction()
        playerFragment.show(transaction, "dialog_playback")
    }

    private fun removeItemDialog(
            recordingItem: RecordingItem,
            context: Context?
    ) {
        val removeDialogFragment: RemoveDialogFragment =
            RemoveDialogFragment()
                    .newInstance(
                            recordingItem.id,
                            recordingItem.filePath)

        val transaction: FragmentTransaction =
                (context as FragmentActivity)
                        .supportFragmentManager
                        .beginTransaction()

        removeDialogFragment.show(transaction, "dialog_remove")
    }

    private fun editItemDialog(
            recordingItem: RecordingItem,
            context: Context?
    ) {

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val charSearch = constraint.toString()

                if (charSearch.isEmpty()) {
                    filterResults.count = dataFullCopy.size
                    filterResults.values = dataFullCopy
                } else {
                    val resultList = ArrayList<RecordingItem>()
                    for (row in dataFullCopy) {
                        if (row.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    filterResults.count = resultList.size
                    filterResults.values = resultList
                }

                return filterResults

            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                data = results?.values as List<RecordingItem>
                notifyDataSetChanged()
            }
        }
    }

}