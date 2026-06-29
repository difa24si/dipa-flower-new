package com.example.dipa_flower.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_news")
data class FavoriteNewsEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val image: String,
    val location: String,
    val date: String,
    val description: String,
    val link: String
)
