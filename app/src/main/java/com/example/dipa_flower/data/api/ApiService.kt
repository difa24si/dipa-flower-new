package com.example.dipa_flower.data.api

import com.example.dipa_flower.data.model.News
import retrofit2.http.GET

interface ApiService {
    @GET("news")
    suspend fun getNewsList(): List<News>
}
