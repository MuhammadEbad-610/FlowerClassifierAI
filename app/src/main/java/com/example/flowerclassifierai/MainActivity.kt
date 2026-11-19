
package com.example.flowerclassifierai

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest

class MainActivity : AppCompatActivity() {

    private lateinit var imagePreview: ImageView

    private val imagePicker = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->

        if (uri != null) {
            imagePreview.setImageURI(uri)
            imagePreview.visibility = ImageView.VISIBLE

            Toast.makeText(
                this,
                "Image selected successfully!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this,
                "No image selected",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSelectImage = findViewById<Button>(R.id.btnSelectImage)
        val btnCamera = findViewById<Button>(R.id.btnCamera)

        imagePreview = findViewById(R.id.imagePreview)

        btnSelectImage.setOnClickListener {

            imagePicker.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        btnCamera.setOnClickListener {
            Toast.makeText(
                this,
                "Camera feature will be added soon!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
