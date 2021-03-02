package com.konovodov.diplomkt.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuoteDao {

    @Query("SELECT * FROM quotes ORDER BY id DESC")
    fun getAll(): LiveData<List<QuoteEntity>>

    @Insert
    fun insert(quote: QuoteEntity)

    @Update
    fun update(quote: QuoteEntity)

    fun save(quote: QuoteEntity) =
        if (quote.id == 0L) insert(quote) else update(quote)

    @Query("DELETE FROM quotes WHERE id = :id")
    fun deleteById(id: Long)

    @Query(
        """
        UPDATE quotes SET
            likes = likes + 1
        WHERE id = :id
        """
    )
    fun likeById(id: Long)

    @Query(
        """
        UPDATE quotes SET
            likes = likes - 1
        WHERE id = :id
        """
    )
    fun dislikeById(id: Long)


    @Query("SELECT * FROM quotes WHERE id=:id")
    fun getById(id: Long): QuoteEntity

}