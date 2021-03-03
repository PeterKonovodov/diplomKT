package com.konovodov.diplomkt.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface QuoteDao {

    @Query("SELECT * FROM quotes ORDER BY likes DESC")
    fun getAll(): LiveData<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE author = :author ORDER BY likes DESC")
    fun getByAuthor(author: String): LiveData<List<QuoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(quote: QuoteEntity)

    @Update
    fun update(quote: QuoteEntity)

    fun save(quote: QuoteEntity) = insert(quote)

/*
    fun save(quote: QuoteEntity) =
            if (quote.id == 0L) insert(quote) else update(quote)
*/

    @Query ("SELECT Count(*) FROM quotes")
    fun getSize() : Int

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