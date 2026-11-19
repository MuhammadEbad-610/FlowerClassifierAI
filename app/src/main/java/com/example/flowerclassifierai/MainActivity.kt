
package com.example.flowerclassifierai

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSelectImage = findViewById<Button>(R.id.btnSelectImage)
        val btnCamera = findViewById<Button>(R.id.btnCamera)

        btnSelectImage.setOnClickListener {
            Toast.makeText(
                this,
                "Image selection will be added soon!",
                Toast.LENGTH_SHORT
            ).show()
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
