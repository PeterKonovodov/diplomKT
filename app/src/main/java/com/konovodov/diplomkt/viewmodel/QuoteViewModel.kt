package com.konovodov.diplomkt.viewmodel

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.konovodov.diplomkt.db.AppDatabase
import com.konovodov.diplomkt.db.QuoteEntity
import com.konovodov.diplomkt.db.QuoteRepository
import com.konovodov.diplomkt.db.QuoteRepositoryRoomImpl
import com.konovodov.diplomkt.dto.Quote

class QuoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuoteRepository = QuoteRepositoryRoomImpl(
        AppDatabase.getInstance(context = application).quoteDao(),
        application
    )

    val allQuotesPaged = repository.getAllPaged(PAGE_SIZE)

    private val allByAuthorPagedMap : HashMap<String, LiveData<PagedList<QuoteEntity>>> = HashMap()

    fun allQuotesByAuthorPaged(author: String) : LiveData<PagedList<QuoteEntity>> {
        return if(allByAuthorPagedMap.containsKey(author))
            allByAuthorPagedMap[author]!!
        else {
            allByAuthorPagedMap[author] = repository.getAllByAuthorPaged(PAGE_SIZE, author)
            allByAuthorPagedMap[author]!!
        }
    }

    fun shareQuote(quote: Quote) = repository.shareQuote(quote)

    fun likeById(id: Long) = repository.likeById(id)
    fun saveQuote(quote: Quote) = repository.saveQuote(quote)
    fun dislikeById(id: Long) = repository.dislikeById(id)
    fun getById(id: Long): Quote = repository.getById(id)
    fun deleteById(id: Long) = repository.deleteById(id)
    fun loadImageByPath(path: String) : Drawable? = repository.loadImageByPath(path)
    fun saveImage(drawable: Drawable): String = repository.saveImage(drawable)

    companion object {
        private const val PAGE_SIZE = 5
    }


}