package com.konovodov.diplomkt.db

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Query
import com.konovodov.diplomkt.dto.Quote
import java.time.LocalDate


interface QuoteRepository {
    fun getList(): LiveData<List<Quote>>
    fun getListByAuthor(author: String): LiveData<List<Quote>>
    fun likeById(id: Long)
    fun dislikeById(id: Long)
    fun shareById(id: Long)
    fun saveQuote(quote: Quote)
    fun getById(id: Long) : Quote
    fun getEmptyQuote() : Quote
}




