package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R

class HistoryActivity : AppCompatActivity() {
    private lateinit var rvHistory: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)
        rvHistory = findViewById(R.id.rv_history)
        rvHistory.setHasFixedSize(true)
        showRecyclerList()
    }
    private fun showRecyclerList() {
        val adapter = HistoryAdapter(ArrayList())
        rvHistory.adapter = adapter
        rvHistory.layoutManager = LinearLayoutManager(this)

        val viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        viewModel.getAllResults().observe(this, Observer { results ->
            results?.let {
                adapter.updateData(it)
            }
        })
    }
}