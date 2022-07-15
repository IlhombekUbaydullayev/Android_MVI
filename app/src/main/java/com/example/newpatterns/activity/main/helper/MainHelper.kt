package com.example.newpatterns.activity.main.helper

import com.example.newpatterns.model.Post

interface MainHelper {
    suspend fun allPosts(): ArrayList<Post>
    suspend fun deletePost(id: Int): Post
    suspend fun createPost(post: Post): Post
}