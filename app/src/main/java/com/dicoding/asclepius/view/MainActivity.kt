package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.AnalysisResultEntity
import com.dicoding.asclepius.data.AppDatabase
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.repository.AnalysisResultRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null
    private var newResult: String = ""

    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var analysisResultRepository: AnalysisResultRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val analysisResultDao = AppDatabase.getDatabase(application).analysisResultDao()
        analysisResultRepository = AnalysisResultRepository(application)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
        binding.newsButton.setOnClickListener {
            val intent = Intent(this@MainActivity, NewsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startGallery() {
        launcherGallery.launch("image/*")
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            startUcropperActivity(uri)
        } else {
            Log.d("Photo Selector", "No media selected")
        }
    }

    private fun startUcropperActivity(uri: Uri) {
        val intent = Intent(this, UcropperActivity::class.java).apply {
            putExtra("imageUri", uri)
        }
        startActivityForResult(intent, UCROP_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCROP_REQUEST && resultCode == Activity.RESULT_OK) {
            val croppedImageUriString = data?.getStringExtra("croppedImageUri")
            if (croppedImageUriString != null) {
                currentImageUri = Uri.parse(croppedImageUriString)
                showImage()
            }
        }
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            Glide.with(this)
                .load(uri)
                .into(binding.previewImageView)
        }
    }

    private fun analyzeImage() {
        currentImageUri?.let { uri ->
            imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        runOnUiThread {
                            showToast(error)
                        }
                    }

                    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                        runOnUiThread {
                            results?.let { classifications ->
                                if (classifications.isNotEmpty() && classifications[0].categories.isNotEmpty()) {
                                    val sortedCategories = classifications[0].categories.sortedByDescending { it?.score }
                                    val displayResult = sortedCategories.joinToString("\n") {
                                        "${it?.label} " + NumberFormat.getPercentInstance().format(it?.score).trim()
                                    }
                                    newResult = displayResult
                                } else {
                                    newResult = ""
                                }

                                currentImageUri?.let { imageUri ->
                                    val timestamp = System.currentTimeMillis()
                                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    val date = dateFormat.format(Date(timestamp))
                                    val analysisResult = AnalysisResultEntity(
                                        imageUri = imageUri.toString(),
                                        date = date,
                                        result = newResult
                                    )
                                    saveAnalysisResult(analysisResult)
                                }

                                moveToResult()
                            }
                        }
                    }
                }
            )
            imageClassifierHelper.classifyStaticImage(uri)
        } ?: run {
            showToast(getString(R.string.empty_image_warning))
        }
    }

    private fun saveAnalysisResult(result: AnalysisResultEntity) {
        val database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "analysis-db").build()
        CoroutineScope(Dispatchers.IO).launch {
            database.analysisResultDao().insertResult(result)
        }
    }

    private fun moveToResult() {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("newResult", newResult)
            putExtra("imageUri", currentImageUri)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val UCROP_REQUEST = 123
    }
}