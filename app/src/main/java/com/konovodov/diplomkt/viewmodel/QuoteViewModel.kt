package com.konovodov.diplomkt.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.konovodov.diplomkt.db.AppDatabase
import com.konovodov.diplomkt.db.QuoteRepository
import com.konovodov.diplomkt.db.QuoteRepositoryRoomImpl
import com.konovodov.diplomkt.dto.Quote

class QuoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuoteRepository = QuoteRepositoryRoomImpl(
        AppDatabase.getInstance(context = application).quoteDao(),
        application
    )

    val data = repository.getAll()

    fun getDataByAuthor(author: String) = repository.getAllByAuthor(author)

    fun shareQuote(quote: Quote) = repository.shareQuote(quote)

    fun likeById(id: Long) = repository.likeById(id)
    fun saveQuote(quote: Quote) = repository.saveQuote(quote)
    fun dislikeById(id: Long) = repository.dislikeById(id)
    fun getEmptyQuote(): Quote = repository.getEmptyQuote()
    fun getById(id: Long): Quote = repository.getById(id)


}