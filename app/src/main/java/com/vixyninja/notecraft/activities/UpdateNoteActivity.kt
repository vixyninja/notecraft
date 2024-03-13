package com.vixyninja.notecraft.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vixyninja.notecraft.R
import com.vixyninja.notecraft.databinding.ActivityUpdateNoteBinding
import com.vixyninja.notecraft.entities.NoteEntity
import com.vixyninja.notecraft.helpers.DatabaseHelper
import com.vixyninja.notecraft.utils.AlertUtil
import java.util.Timer
import java.util.TimerTask

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var dbHelper: DatabaseHelper
    private var note: NoteEntity? = null

    companion object {
        const val TAG = "UpdateNoteActivity"
        const val NOTE = "note"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_update_note)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            insets
        }

        dbHelper = DatabaseHelper(context = this)
        intent.getIntExtra(NOTE, -1).let {
            note = dbHelper.getNoteById(it)
        }
        bindViews()
    }

    private fun bindViews() {
        binding.idNoteTitle.setText(note?.title ?: "")
        binding.idNoteDescription.setText(note?.content ?: "")
        binding.idUpdateNoteIcon.setOnClickListener {
            val title = binding.idNoteTitle.text.toString()
            val content = binding.idNoteDescription.text.toString()
            if (validateInput(title, content)) {
                note?.copy(
                    title = title, content = content
                )?.let { it1 ->
                    dbHelper.updateById(
                        it1.id!!, it1.title, it1.content
                    )
                }
                AlertUtil(this).showBar(binding.root, "Note updated")
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        finish()
                    }
                }, 1000)
            }
        }
    }

    private fun validateInput(title: String, content: String): Boolean {
        return title.isNotEmpty() && content.isNotEmpty()
    }

}