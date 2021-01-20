package com.example.vaytsu_voicerecorder.listRecord

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.vaytsu_voicerecorder.R
import com.example.vaytsu_voicerecorder.database.RecordingItem
import com.example.vaytsu_voicerecorder.listRecord.simpleOperations.RemoveDialogFragment
import com.example.vaytsu_voicerecorder.player.PlayerFragment
import java.io.File
import java.lang.Exception
import java.util.concurrent.TimeUnit

class ListRecordAdapter: RecyclerView.Adapter<ListRecordAdapter.ViewHolder>() {

    var data = listOf<RecordingItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var vName: TextView = itemView.findViewById(R.id.fileNameTextView)
        var vLength: TextView = itemView.findViewById(R.id.fileLengthTextView)
        var cardView: View = itemView.findViewById(R.id.cardView)
        var deleteCardIV: ImageView = itemView.findViewById(R.id.deleteCardIV)
        var editCardIV: ImageView = itemView.findViewById(R.id.editCardIV)

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
            //false
        }

        holder.editCardIV.setOnClickListener {
            editItemDialog(recordingItem, context)
        }
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

}