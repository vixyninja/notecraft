package com.vixyninja.notecraft.activities

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vixyninja.notecraft.R
import com.vixyninja.notecraft.databinding.ActivityAddNoteBinding
import com.vixyninja.notecraft.entities.NoteEntity
import com.vixyninja.notecraft.helpers.DatabaseHelper
import com.vixyninja.notecraft.utils.AlertUtil
import java.util.Timer
import java.util.TimerTask

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_add_note)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            WindowInsetsCompat.CONSUMED
        }
        dbHelper = DatabaseHelper(context = this)
        bindViews(this)
    }

    private fun bindViews(context: Context) {
        binding.idAddNoteIcon.setOnClickListener {
            val title = binding.idNoteTitle.text.toString()
            val content = binding.idNoteDescription.text.toString()

            if (validateInput(title, content)) {
                val noteEntity: NoteEntity = NoteEntity(title = title, content = content)
                dbHelper.insertNote(noteEntity)
                AlertUtil(this).showBar(binding.root, "Note add")
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        finish()
                    }
                }, 1000)
            } else {
                AlertUtil(context).showError(title = "Error",
                    message = "Title and content cannot be empty",
                    callback = {})
            }
        }
    }

    private fun validateInput(title: String, content: String): Boolean {
        return title.isNotEmpty() && content.isNotEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}