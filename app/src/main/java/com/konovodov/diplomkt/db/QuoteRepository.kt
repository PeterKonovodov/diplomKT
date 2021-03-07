package com.konovodov.diplomkt.db

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import com.konovodov.diplomkt.dto.Quote


interface QuoteRepository {
    fun getAll(): LiveData<List<Quote>>
    fun getAllByAuthor(author: String): LiveData<List<Quote>>
    fun likeById(id: Long)
    fun dislikeById(id: Long)
    fun shareQuote(quote: Quote)
    fun saveQuote(quote: Quote)
    fun getById(id: Long): Quote
    fun deleteById(id: Long)
    fun getEmptyQuote(): Quote
    fun getImageById(id: Long) : Drawable?
}




