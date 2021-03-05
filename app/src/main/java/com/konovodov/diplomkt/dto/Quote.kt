package com.konovodov.diplomkt.dto

import android.graphics.drawable.Drawable
import android.net.Uri
import java.time.LocalDateTime
import java.time.ZoneOffset

data class Quote (
        val id: Long = 0,
        val author: String = "",
        val fromAuthor: String = "",
        val published: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
        val content: String = "",
        val link: String = "",
        val likes: Int = 0,
        val imagePath: String = "",
        val imageDrawable: Drawable? = null
){}