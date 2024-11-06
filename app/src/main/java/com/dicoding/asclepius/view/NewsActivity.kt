package com.dicoding.asclepius.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.api.ApiConfig
import com.dicoding.asclepius.api.NewsResponse
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var adapter: NewsAdapter
    private lateinit var progressBar: ProgressBar

    companion object {
        private const val TAG = "NewsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        adapter = NewsAdapter()
        binding.recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(itemDecoration)

        progressBar = findViewById(R.id.progressBar)

        findNews()
    }

    private fun findNews() {
        progressBar.visibility = View.VISIBLE

        val client = ApiConfig.getApiService().getNews()
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        adapter.submitList(responseBody.articles)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}

