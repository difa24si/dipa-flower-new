package com.example.dipa_flower.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteNewsDao {
    @Query("SELECT * FROM favorite_news")
    suspend fun getAllFavoriteNews(): List<FavoriteNewsEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_news WHERE id = :newsId)")
    suspend fun isFavorite(newsId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(news: FavoriteNewsEntity)

    @Delete
    suspend fun deleteFavorite(news: FavoriteNewsEntity)
}
