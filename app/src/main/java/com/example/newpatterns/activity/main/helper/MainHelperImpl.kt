package com.example.newpatterns.activity.main.helper

import com.example.newpatterns.model.Post
import com.example.newpatterns.network.service.PostService

class MainHelperImpl(private val postService: PostService) : MainHelper {

    override suspend fun allPosts(): ArrayList<Post> {
        return postService.allPosts()
    }

    override suspend fun deletePost(id: Int): Post {
        return postService.deletePost(id)
    }

    override suspend fun createPost(post: Post): Post {
        return postService.createPost(post)
    }
}