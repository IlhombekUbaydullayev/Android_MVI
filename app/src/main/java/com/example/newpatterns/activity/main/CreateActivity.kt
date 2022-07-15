package com.example.newpatterns.activity.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newpatterns.R
import com.example.newpatterns.activity.main.helper.MainHelperImpl
import com.example.newpatterns.activity.main.intentstate.CreateIntent
import com.example.newpatterns.activity.main.intentstate.CreateState
import com.example.newpatterns.activity.main.intentstate.MainIntent
import com.example.newpatterns.activity.main.viewmodel.MainViewModel
import com.example.newpatterns.activity.main.viewmodel.MainViewModelFactory
import com.example.newpatterns.adapter.CreateAdapter
import com.example.newpatterns.model.Post
import com.example.newpatterns.network.RetrofitBuilder
import com.example.newpatterns.utils.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class CreateActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var b_floating: FloatingActionButton
    lateinit var b_floatingtwo: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        initViews()
        observeViewModel()
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
        observeViewModel()
    }

    private fun initViews() {
        val factory = MainViewModelFactory(MainHelperImpl(RetrofitBuilder.POST_SERVICE))
        viewModel = ViewModelProvider(this,factory).get(MainViewModel::class.java)

        recyclerView = findViewById(R.id.rv_create)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        b_floating = findViewById(R.id.b_floating)
        b_floating.setOnClickListener {
            apiCreatePost()
        }
        b_floatingtwo = findViewById(R.id.b_floatingtwo)
        b_floatingtwo.setOnClickListener {
            openleftAvctivity()
        }
        intentAllPosts()
    }

    private fun openleftAvctivity() {
        finish()
    }

    fun apiCreatePost() {
        val title = "Create Poster"
        val body = "Do you want to create?"
        val inflate = R.layout.alert_dialog_background
        val view = LayoutInflater.from(this).inflate(inflate,null,false)

        Utils.customDialogCreate(this,view, title, body, object : Utils.DialogListener {
            @SuppressLint("StaticFieldLeak")
            override fun onPositiveClick() {
                val tv_title = view.findViewById<TextInputEditText>(R.id.et_titles)
                val tv_body = view.findViewById<TextInputEditText>(R.id.et_bodys)
                var user = Post(0,0,tv_title.text.toString(),tv_body.text.toString())
                lifecycleScope.launch {
                    viewModel.post = user
                    Log.d("POST",viewModel.post.toString())
                    viewModel.createState.send(CreateIntent.CreatePost)
                }
            }

            override fun onNegativeClick() {

            }
        })
    }

    private fun intentAllPosts() {
        lifecycleScope.launch {
            viewModel.createState.send(CreateIntent.AllPosts)
        }
    }

    private fun refreshAdapter(posters: ArrayList<Post>) {
        val adapter = CreateAdapter(this, posters)
        recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.crState.collect {
                when (it) {
                    is CreateState.Init -> {
                        Log.d("@@@", "Init")
                    }
                    is CreateState.Loading -> {
                        Log.d("@@@", "Loading")
                    }
                    is CreateState.AllPosts -> {
                        Log.d("@@@", "PostList" + it.posts)
                        refreshAdapter(it.posts)
                    }
                    is CreateState.CreatePost -> {
                        Log.d("@@@", "CreatePost "+it.post)
                        intentAllPosts()
                    }
                    is CreateState.Error -> {
                        Log.d("@@@", "Error " + it)
                    }
                }
            }
        }
    }
}