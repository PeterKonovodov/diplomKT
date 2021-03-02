package com.konovodov.diplomkt.db

import androidx.lifecycle.Transformations
import com.konovodov.diplomkt.db.QuoteDao
import com.konovodov.diplomkt.dto.Quote

class QuoteRepositoryRoomImpl(private val dao: QuoteDao) : QuoteRepository {

    override fun getList() = Transformations.map(dao.getAll()) { list ->
        list.map {
            Quote(
                it.id,
                it.author,
                it.published,
                it.content,
                it.imagePath,
                it.link,
                it.likes
            )
        }
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun dislikeById(id: Long) {
        dao.dislikeById(id)
    }

    override fun shareById(id: Long) {
    }

    override fun saveQuote(quote: Quote) {
        dao.save(QuoteEntity.fromDto(quote))
    }

    override fun getById(id: Long): Quote? {
        return dao.getById(id).toDto()
    }

    override fun getEmptyQuote(): Quote = Quote(id = 0)

}