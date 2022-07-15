package com.example.newpatterns.activity.main.intentstate

import com.example.newpatterns.model.Post

sealed class CreateState {
    object Init : CreateState()
    object Loading : CreateState()

    data class AllPosts(val posts: ArrayList<Post>) : CreateState()
    data class CreatePost(val post: Post) : CreateState()

    data class Error(val error: String?) : CreateState()
}