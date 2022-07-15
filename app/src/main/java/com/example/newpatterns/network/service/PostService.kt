package com.example.newpatterns.network.service

import com.example.newpatterns.model.Post
import retrofit2.Call
import retrofit2.http.*

interface PostService {

    @GET("posts")
    suspend fun allPosts(): ArrayList<Post>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int): Post

    @POST("posts")
    suspend fun createPost(@Body post: Post): Post

}