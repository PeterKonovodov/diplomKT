package com.konovodov.diplomkt.dto

import java.time.LocalDateTime
import java.time.ZoneOffset

data class Quote (
        val id: Long = 0,
        val author: String = "",
        val published: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
        val content: String = "",
        val imagePath: String = "",
        val link: String = "",
        val likes: Int = 0,
){}