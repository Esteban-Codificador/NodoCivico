package com.nodocivico.app.network

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("posts")
    fun getPosts(): Call<List<Post>>
}