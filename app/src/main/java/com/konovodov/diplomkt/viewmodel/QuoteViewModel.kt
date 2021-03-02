package com.konovodov.diplomkt.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.konovodov.diplomkt.db.AppDatabase
import com.konovodov.diplomkt.db.QuoteRepository
import com.konovodov.diplomkt.db.QuoteRepositoryRoomImpl
import com.konovodov.diplomkt.dto.Quote

class QuoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuoteRepository = QuoteRepositoryRoomImpl(AppDatabase.getInstance(context = application).noteDao())

    val data = repository.getList()
    fun likeById(id: Long) = repository.likeById(id)
    fun saveQuote(quote: Quote) = repository.saveQuote(quote)
    fun dislikeById(id: Long) = repository.dislikeById(id)
    fun shareById(id: Long) = repository.shareById(id)

/*
    fun getById(id: Long): Note? = repository.getById(id)
    fun getEmptyNote(): Note = repository.getEmptyNote()
*/
}