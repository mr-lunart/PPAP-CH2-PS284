package com.example.ppaps.ui.verification

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.ppaps.R
import com.example.ppaps.data.ResultState
import com.example.ppaps.databinding.ActivityVerificationBinding
import com.example.ppaps.ui.ViewModelFactory
import com.example.ppaps.ui.createCustomTempFile
import com.example.ppaps.ui.main.MainActivity
import com.example.ppaps.ui.reduceFileImage
import com.example.ppaps.ui.signin.SigninActivity
import com.example.ppaps.ui.signup.SignupViewModel
import com.example.ppaps.ui.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class VerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private var imageCapture: ImageCapture? = null
    private var user_id: String = ""
    private var count = 0
    private val viewModel by viewModels<VerificationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Camera permission granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.face_verif)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.captureImage.setOnClickListener {
            takePhoto()
        }

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
            startCamera()
        }

        getUserData()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewfinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                showToast("Gagal memunculkan kamera")
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createCustomTempFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    showToast("Gagal mengambil gambar")
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val photoUri = output.savedUri ?: Uri.fromFile(photoFile)
                    val imageFile = uriToFile(photoUri, this@VerificationActivity).reduceFileImage()

                    val requestBody = user_id.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "face_photo",
                        imageFile.name,
                        requestImageFile
                    )

                    if (count < 4) {
                        lifecycleScope.launch {
                            viewModel.upload(multipartBody, requestBody).observe(this@VerificationActivity) {
                                when (it) {
                                    is ResultState.Success -> {
                                        showLoading(false)
                                        showToast("Foto berhasil, silahkan foto kembali")
                                        count += 1
                                    }
                                    is ResultState.Loading -> { showLoading(true) }
                                    is ResultState.Error -> {
                                        showLoading(false)
                                        showToast(it.message!!)
                                    }
                                    else -> {}
                                }
                            }
                        }
                    } else {
                        lifecycleScope.launch {
                            viewModel.confirm(user_id).observe(this@VerificationActivity) {
                                when (it) {
                                    is ResultState.Success -> {
                                        showLoading(false)
                                        val intent = Intent(this@VerificationActivity, MainActivity::class.java)
                                        intent.putExtra("result", "true")
                                        startActivity(intent)
                                    }
                                    is ResultState.Loading -> { showLoading(true) }
                                    is ResultState.Error -> {
                                        showLoading(false)
                                        showToast(it.message!!)
                                    }
                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getUserData() {
        viewModel.getSession().observe(this@VerificationActivity) {
            lifecycleScope.launch {
                viewModel.getUser(it.token).observe(this@VerificationActivity) {
                    when (it) {
                        is ResultState.Success -> {
                            user_id = it.data.user?.idUser!!
                        }
                        is ResultState.Loading -> {  }
                        is ResultState.Error -> {
                            showToast(it.message!!)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        private const val TAG = "VerificationActivity"
    }
}