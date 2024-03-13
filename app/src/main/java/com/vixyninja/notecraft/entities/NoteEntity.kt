package com.vixyninja.notecraft.entities

import java.util.Date

data class NoteEntity(
    val id: Int? = null,
    val title: String,
    val content: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
) {
    override fun toString(): String {
        return "NoteEntity(id=$id, title='$title', content='$content', createAt=$createdAt, updateAt=$updatedAt)"
    }
}
