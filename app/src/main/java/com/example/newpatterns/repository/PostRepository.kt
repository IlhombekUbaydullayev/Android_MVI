package com.example.newpatterns.repository

import com.example.newpatterns.activity.main.helper.MainHelper
import com.example.newpatterns.model.Post
import retrofit2.http.POST

class PostRepository(private val mainHelper: MainHelper) {

    suspend fun allPosts() = mainHelper.allPosts()
    suspend fun deletePost(id: Int) = mainHelper.deletePost(id)
    suspend fun createPost(post: Post) = mainHelper.createPost(post)
}