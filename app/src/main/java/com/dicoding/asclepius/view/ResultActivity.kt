package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = intent.getStringExtra(EXTRA_RESULT)
        binding.resultText.text = getString(R.string.hasil_analisis, result)

        val imageUri = intent.getParcelableExtra<Uri>(EXTRA_IMAGE_URI)
        binding.resultImage.setImageURI(imageUri)

        binding.btnHistory.setOnClickListener {
            val intent = Intent(this@ResultActivity, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_RESULT = "newResult"
        const val EXTRA_IMAGE_URI = "imageUri"
    }
}