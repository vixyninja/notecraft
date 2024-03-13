package com.vixyninja.notecraft.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.vixyninja.notecraft.entities.NoteEntity
import java.time.LocalDateTime

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "note_craft.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_CREATED_AT = "created_at"
        private const val COLUMN_UPDATED_AT = "updated_at"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createAt = LocalDateTime.now()
        val updatedAt = LocalDateTime.now()
        val sqlQuery: String =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_CREATED_AT TEXT DEFAULT '$createAt', $COLUMN_UPDATED_AT TEXT DEFAULT '$updatedAt')"

        db?.execSQL(/* sql = */ sqlQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val sqlQuery: String = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(/* sql = */ sqlQuery)
        onCreate(db)
    }


    fun insertNote(node: NoteEntity) {
        val db = this.writableDatabase
        val values: ContentValues = ContentValues().apply {
            put(COLUMN_TITLE, node.title)
            put(COLUMN_CONTENT, node.content)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateNote(node: NoteEntity) {
        val db = this.writableDatabase
        val values: ContentValues = ContentValues().apply {

        }
    }

    fun getAllNotes(): List<NoteEntity> {
        val notes: MutableList<NoteEntity> = mutableListOf()
        val db = this.readableDatabase
        val sqlQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(/* sql = */ sqlQuery, /* selectionArgs = */ null)
        while (cursor.moveToNext()) {
            val note = NoteEntity(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                createdAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)),
                updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_AT))
            )
            notes.add(note)
        }
        cursor.close()
        db.close()
        return notes
    }

    fun getNoteById(id: Int): NoteEntity? {
        var note: NoteEntity? = null
        val db = this.readableDatabase
        db.query( /* table = */ TABLE_NAME, /* columns = */
            null, /* selection = */
            "$COLUMN_ID = ?", /* selectionArgs = */
            arrayOf(id.toString()), /* groupBy = */
            null, /* having = */
            null, /* orderBy = */
            null
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                note = NoteEntity(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                    createdAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)),
                    updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_AT))
                )
            }
        }
        db.close()
        return note
    }

    fun updateById(id: Int, title: String, content: String) {
        val db = this.writableDatabase
        db.query( /* table = */ TABLE_NAME, /* columns = */
            null, /* selection = */
            "$COLUMN_ID = ?", /* selectionArgs = */
            arrayOf(id.toString()), /* groupBy = */
            null, /* having = */
            null, /* orderBy = */
            null
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                val values: ContentValues = ContentValues().apply {
                    put(COLUMN_TITLE, title)
                    put(COLUMN_CONTENT, content)
                    put(COLUMN_UPDATED_AT, LocalDateTime.now().toString())
                }
                db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
            }
        }
        db.close()
    }

    fun deleteById(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}