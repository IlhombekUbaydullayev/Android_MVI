package com.example.newpatterns.activity.main.intentstate

sealed class CreateIntent {
    object AllPosts : CreateIntent()
    object CreatePost : CreateIntent()
}