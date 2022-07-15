package com.example.newpatterns.activity.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newpatterns.activity.main.intentstate.CreateIntent
import com.example.newpatterns.activity.main.intentstate.CreateState
import com.example.newpatterns.activity.main.intentstate.MainIntent
import com.example.newpatterns.activity.main.intentstate.MainState
import com.example.newpatterns.model.Post
import com.example.newpatterns.repository.PostRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch


class MainViewModel(private val repository: PostRepository) : ViewModel() {
    val createState = Channel<CreateIntent>(Channel.UNLIMITED)
    private val _crState = MutableStateFlow<CreateState>(CreateState.Init)
    val crState: StateFlow<CreateState> get() = _crState

    val mainIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Init)
    val state: StateFlow<MainState> get() = _state


    var postId: Int = 0
    var post: Post = Post()

    init {
        handleIntent()
        handleIntentSec()
    }

    private fun handleIntentSec() {
        viewModelScope.launch {
            createState.consumeAsFlow().collect{
                when(it) {
                    is CreateIntent.AllPosts -> apiAllCreatePosts()
                    is CreateIntent.CreatePost -> apiCreatePost()
                }
            }
        }
    }

    private fun handleIntent() {
        viewModelScope.launch {
            mainIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.AllPosts -> apiAllPosts()
                    is MainIntent.DeletePost -> apiDeletePost()
                }
            }
        }
    }

    private fun apiAllPosts() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            _state.value = try {
                MainState.AllPosts(repository.allPosts())
            } catch (e: Exception) {
                MainState.Error(e.localizedMessage)
            }
        }
    }

    private fun apiAllCreatePosts() {
        viewModelScope.launch {
            _crState.value = CreateState.Loading
            _crState.value = try {
                CreateState.AllPosts(repository.allPosts())
            } catch (e: Exception) {
                CreateState.Error(e.localizedMessage)
            }
        }
    }

    private fun apiDeletePost() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            _state.value = try {
                MainState.DeletePost(repository.deletePost(postId))
            } catch (e: Exception) {
                MainState.Error(e.localizedMessage)
            }
        }
    }

     fun apiCreatePost() {
        viewModelScope.launch {
            _crState.value = CreateState.Loading
            _crState.value = try {
                CreateState.CreatePost(repository.createPost(post))
            }catch (e: Exception) {
                CreateState.Error(e.localizedMessage)
            }
        }
    }
}