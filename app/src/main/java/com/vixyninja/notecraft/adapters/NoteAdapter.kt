package com.vixyninja.notecraft.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vixyninja.notecraft.R
import com.vixyninja.notecraft.activities.UpdateNoteActivity
import com.vixyninja.notecraft.entities.NoteEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteAdapter(private var notes: List<NoteEntity>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {


    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById<TextView?>(R.id.idTitle)
        private val contentTextView: TextView = itemView.findViewById<TextView?>(R.id.idContent)
        private val createdTextView: TextView = itemView.findViewById<TextView?>(R.id.idCreatedDate)
        private val updatedTextView: TextView = itemView.findViewById<TextView?>(R.id.idUpdatedDate)
        private var currentNote: NoteEntity? = null

        init {
            itemView.setOnClickListener {
                currentNote?.let { onItemClickListener() }
            }
        }

        private fun onItemClickListener() {
            val intent = Intent(itemView.context, UpdateNoteActivity::class.java)
            with(currentNote) {
                intent.putExtra(UpdateNoteActivity.NOTE, currentNote?.id)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(note: NoteEntity) {
            with(note) {
                currentNote = note
                titleTextView.text = note.title
                contentTextView.text = note.content
                val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
                createdTextView.text = buildString {
                    append("Created: ")
                    append(
                        LocalDateTime.parse(note.createdAt, dateFormat)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                }
                updatedTextView.text = buildString {
                    append("Updated: ")
                    append(
                        LocalDateTime.parse(note.updatedAt, dateFormat)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.NoteViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }


    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(notes: List<NoteEntity>) {
        this.notes = notes
        notifyDataSetChanged()
    }

}