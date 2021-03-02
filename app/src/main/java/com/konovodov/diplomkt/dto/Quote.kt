package com.konovodov.diplomkt.dto

data class Quote (
val id: Long = 1,
val author: String = "",
val published: Long = 0,
val content: String = "",
val imagePath: String = "",
val link: String = "",
val likes: Int = 0,
){}