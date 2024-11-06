package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.asclepius.R
import com.yalantis.ucrop.UCrop
import java.io.File

class UcropperActivity : AppCompatActivity() {

    private val REQUEST_UCROP = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ucropper)

        val imageUri: Uri? = intent.getParcelableExtra("imageUri")
        imageUri?.let { uri ->
            startUCropActivity(uri)
        }
    }

    private fun startUCropActivity(uri: Uri) {
        val filename = "cropped_image_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(getExternalFilesDir(null), filename))

        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .start(this, REQUEST_UCROP)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_UCROP) {
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = UCrop.getOutput(data!!)
                if (resultUri != null) {
                    val intent = Intent().apply {
                        putExtra("croppedImageUri", resultUri.toString())
                    }
                    setResult(Activity.RESULT_OK, intent)
                }
            } else if (resultCode == UCrop.RESULT_ERROR) {
                val error = UCrop.getError(data!!)
                Log.e("UCrop Error", "Error: $error")
            }
        }
        finish()
    }
}