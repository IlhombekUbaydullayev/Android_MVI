package com.example.newpatterns.network

import com.example.newpatterns.network.service.PostService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

//    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    private const val BASE_URL = "https://62c58071a361f7251286730b.mockapi.io/"
    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val POST_SERVICE: PostService = getRetrofit().create(PostService::class.java)

}