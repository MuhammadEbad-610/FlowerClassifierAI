
package com.example.flowerclassifierai

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var imagePreview: ImageView
    private lateinit var cameraPreview: PreviewView
    private lateinit var btnCapture: Button

    private var imageCapture: ImageCapture? = null

    private val imagePicker = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imagePreview.setImageURI(uri)
            imagePreview.visibility = ImageView.VISIBLE
            cameraPreview.visibility = PreviewView.GONE
            btnCapture.visibility = Button.GONE

            Toast.makeText(
                this,
                "Image selected successfully!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startCamera()
        } else {
            Toast.makeText(
                this,
                "Camera permission is required.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSelectImage = findViewById<Button>(R.id.btnSelectImage)
        val btnCamera = findViewById<Button>(R.id.btnCamera)

        btnCapture = findViewById(R.id.btnCapture)
        imagePreview = findViewById(R.id.imagePreview)
        cameraPreview = findViewById(R.id.cameraPreview)

        btnSelectImage.setOnClickListener {
            imagePicker.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        btnCamera.setOnClickListener {
            checkCameraPermission()
        }

        btnCapture.setOnClickListener {
            takePhoto()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        cameraPreview.visibility = PreviewView.VISIBLE
        btnCapture.visibility = Button.VISIBLE
        imagePreview.visibility = ImageView.GONE

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.surfaceProvider = cameraPreview.surfaceProvider

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exception: Exception) {
                Toast.makeText(
                    this,
                    "Unable to start camera: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val capture = imageCapture ?: return

        val photoFile = File(
            cacheDir,
            "flower_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            photoFile
        ).build()

        capture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(
                    outputFileResults: ImageCapture.OutputFileResults
                ) {
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)

                    imagePreview.setImageBitmap(bitmap)
                    imagePreview.visibility = ImageView.VISIBLE

                    cameraPreview.visibility = PreviewView.GONE
                    btnCapture.visibility = Button.GONE

                    Toast.makeText(
                        this@MainActivity,
                        "Flower photo captured!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@MainActivity,
                        "Photo capture failed: ${exception.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
}
