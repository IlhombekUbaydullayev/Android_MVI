package com.example.newpatterns.activity.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newpatterns.R
import com.example.newpatterns.activity.main.helper.MainHelperImpl
import com.example.newpatterns.activity.main.intentstate.MainIntent
import com.example.newpatterns.activity.main.intentstate.MainState
import com.example.newpatterns.activity.main.viewmodel.MainViewModel
import com.example.newpatterns.activity.main.viewmodel.MainViewModelFactory
import com.example.newpatterns.adapter.PostAdapter
import com.example.newpatterns.model.Post
import com.example.newpatterns.network.RetrofitBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

//https://abhiappmobiledeveloper.medium.com/android-mvi-reactive-architecture-pattern-74e5f1300a87

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var b_floating_create: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        observeViewModel()
        Log.d("LOGS","onCreate")
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        Log.d("LOGS","onCreateView")
        return super.onCreateView(name, context, attrs)
    }

    private fun initViews() {
        val factory = MainViewModelFactory(MainHelperImpl(RetrofitBuilder.POST_SERVICE))
        viewModel = ViewModelProvider(this,factory).get(MainViewModel::class.java)
        b_floating_create = findViewById(R.id.b_floating_create)
        b_floating_create.setOnClickListener {
            openCrreateActivty()
        }
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        intentAllPosts()
    }

    private fun openCrreateActivty() {
        val intent = Intent(this,CreateActivity::class.java)
        startActivity(intent)
    }

    private fun intentAllPosts() {
        lifecycleScope.launch {
            viewModel.mainIntent.send(MainIntent.AllPosts)
        }
    }

    fun intentDeletePost(id: Int) {
        lifecycleScope.launch {
            viewModel.postId = id
            viewModel.mainIntent.send(MainIntent.DeletePost)
        }
    }

    private fun refreshAdapter(posters: ArrayList<Post>) {
        val adapter = PostAdapter(this, posters)
        recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is MainState.Init -> {
                        Log.d("@@@", "Init")
                    }
                    is MainState.Loading -> {
                        Log.d("@@@", "Loading")
                    }
                    is MainState.AllPosts -> {
                        Log.d("@@@", "PostList" + it.posts)
                        refreshAdapter(it.posts)
                    }
                    is MainState.DeletePost -> {
                        Log.d("@@@", "DeletePost "+it.post)
                        intentAllPosts()
                    }
                    is MainState.Error -> {
                        it
                        Log.d("@@@", "Error " + it)
                    }
                }
            }
        }
    }
}