package com.example.newpatterns.activity.main.intentstate

sealed class MainIntent {
    object AllPosts : MainIntent()
    object DeletePost : MainIntent()
}