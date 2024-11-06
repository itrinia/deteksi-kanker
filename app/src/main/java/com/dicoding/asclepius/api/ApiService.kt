package com.dicoding.asclepius.api

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("v2/top-headlines?q=cancer&category=health&language=en")
    fun getNews(
    ): Call<NewsResponse>
}