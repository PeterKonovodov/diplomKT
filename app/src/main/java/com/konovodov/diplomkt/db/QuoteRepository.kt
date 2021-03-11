package com.konovodov.diplomkt.db

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.konovodov.diplomkt.dto.Quote


interface QuoteRepository {
    fun getAllPaged(pageSize: Int): LiveData<PagedList<QuoteEntity>>
    fun getAllByAuthorPaged(pageSize: Int, author: String): LiveData<PagedList<QuoteEntity>>
    fun likeById(id: Long)
    fun dislikeById(id: Long)
    fun shareQuote(quote: Quote)
    fun saveQuote(quote: Quote)
    fun getById(id: Long): Quote
    fun deleteById(id: Long)
    fun getEmptyQuote(): Quote
    fun loadImageByPath(path: String) : Drawable?
    fun saveImage(drawable: Drawable) : String
}




