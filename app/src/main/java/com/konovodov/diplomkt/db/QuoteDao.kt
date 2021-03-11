package com.konovodov.diplomkt.db

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.room.*

@Dao
interface QuoteDao {

    @Query("SELECT * FROM quotes ORDER BY likes DESC")
    fun getAllPaged(): DataSource.Factory<Int, QuoteEntity>

    @Query("SELECT * FROM quotes WHERE author = :author ORDER BY likes DESC")
    fun getAllByAuthorPaged(author: String): DataSource.Factory<Int, QuoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(quote: QuoteEntity)

    @Update
    fun update(quote: QuoteEntity)

    fun save(quote: QuoteEntity) = insert(quote)

    @Query("SELECT Count(*) FROM quotes")
    fun getSize(): Int

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