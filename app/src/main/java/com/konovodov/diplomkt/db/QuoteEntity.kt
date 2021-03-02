package com.konovodov.diplomkt.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.konovodov.diplomkt.dto.Quote

@Entity(tableName = "quotes")
data class QuoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 1,
    val author: String = "",
    val published: Long = 0,
    val content: String = "",
    val imagePath: String = "",
    val link: String = "",
    val likes: Int = 0,
) {
    fun toDto() = Quote(
        this.id,
        this.author,
        this.published,
        this.content,
        this.imagePath,
        this.link,
        this.likes,
    )

    companion object {
        fun fromDto(dto: Quote) =
            QuoteEntity(
                dto.id,
                dto.author,
                dto.published,
                dto.content,
                dto.imagePath,
                dto.link,
                dto.likes
            )
    }
}
