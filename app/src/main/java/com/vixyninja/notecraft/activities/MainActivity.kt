package com.vixyninja.notecraft.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vixyninja.notecraft.R
import com.vixyninja.notecraft.adapters.NoteAdapter
import com.vixyninja.notecraft.adapters.SwipeGesture
import com.vixyninja.notecraft.databinding.ActivityMainBinding
import com.vixyninja.notecraft.entities.NoteEntity
import com.vixyninja.notecraft.helpers.DatabaseHelper
import com.vixyninja.notecraft.utils.AlertUtil


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var notesAdapter: NoteAdapter
    private lateinit var notes: List<NoteEntity>
    private var isLinear = true

    companion object {
        private const val TAG = "MainActivity"
        private const val LAYOUT = "layout"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main)) { v, windowInsets ->
            val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
        bindViews(this)
        dbHelper = DatabaseHelper(context = this)
        notes = dbHelper.getAllNotes()
        notesAdapter = NoteAdapter(notes)
        binding.idListNotes.adapter = notesAdapter
        binding.idListNotes.setHasFixedSize(true)
        binding.idListNotes.setItemViewCacheSize(20)
        expandedLinear()
        binding.idChangeLayout.setOnClickListener {
            if (isLinear) {
                expandedGrid()
            } else {
                expandedLinear()
            }
        }
    }

    private fun bindViews(context: Context): Unit {
        binding.addButton.setOnClickListener {
            val intent: Intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun expandedGrid() {
        binding.idListNotes.layoutManager = GridLayoutManager(this, 2)
        isLinear = false
        val swipeGesture: SwipeGesture = swipeGesture(this)
        val itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(binding.idListNotes)
    }

    private fun expandedLinear() {
        binding.idListNotes.layoutManager = LinearLayoutManager(this)
        isLinear = true
        val swipeGesture: SwipeGesture = swipeGesture(this)
        val itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(binding.idListNotes)
    }


    private fun swipeGesture(context: Context): SwipeGesture {
        var swipeGesture: SwipeGesture
        object : SwipeGesture() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        onDeleted(viewHolder, notes[position].id!!)
                    }

                    ItemTouchHelper.RIGHT -> {
                        onUpdated(notes[position].id!!)
                    }
                }
            }
        }.also { swipeGesture = it }
        return swipeGesture
    }

    private fun onDeleted(viewHolder: RecyclerView.ViewHolder, position: Int) {
        dbHelper.deleteById(position)
        notes = dbHelper.getAllNotes()
        notesAdapter.refreshData(notes)
        AlertUtil(this).showBar(viewHolder.itemView, "Note deleted")
    }

    private fun onUpdated(position: Int) {
        val intent: Intent = Intent(this, UpdateNoteActivity::class.java)
        with(position) {
            intent.putExtra(UpdateNoteActivity.NOTE, position)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        notes = dbHelper.getAllNotes()
        notesAdapter.refreshData(notes)
    }

    override fun onStop() {
        super.onStop()
        notes = dbHelper.getAllNotes()
        notesAdapter.refreshData(notes)
    }

    override fun onRestart() {
        super.onRestart()
        if (isLinear) {
            expandedLinear()
        } else {
            expandedGrid()
        }
        notes = dbHelper.getAllNotes()
        notesAdapter.refreshData(notes)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isLinear = savedInstanceState.getBoolean(LAYOUT)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TAG, "onSaveInstanceState")
        outState.putBoolean(LAYOUT, isLinear)
    }


    override fun onPause() {
        super.onPause()
        notes = dbHelper.getAllNotes()
        notesAdapter.refreshData(notes)
    }

    override fun onDestroy() {
        super.onDestroy()
        notes = dbHelper.getAllNotes()
        dbHelper.close()
    }

}