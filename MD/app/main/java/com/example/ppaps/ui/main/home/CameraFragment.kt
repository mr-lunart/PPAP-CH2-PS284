package com.example.ppaps.ui.main.home

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.ppaps.R
import com.example.ppaps.data.ResultState
import com.example.ppaps.databinding.FragmentCameraBinding
import com.example.ppaps.databinding.FragmentHomeBinding
import com.example.ppaps.ui.ViewModelFactory
import com.example.ppaps.ui.createCustomTempFile
import com.example.ppaps.ui.reduceFileImage
import com.example.ppaps.ui.uriToFile
import com.example.ppaps.ui.verification.VerificationActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private var imageCapture: ImageCapture? = null
    private var user_id = ""
    private val viewModel by viewModels<CameraViewModel> {
        ViewModelFactory.getInstance(requireContext())
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
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val gradientDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_bg)
        view?.background = gradientDrawable

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getToken()

        val toolbar = binding.myToolbar
        toolbar.title = "Panggilan Darurat"
        toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
            startCamera()
        }

        binding.captureImage.setOnClickListener {
            takePhoto()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

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
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createCustomTempFile(requireContext())
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    showToast("Gagal mengambil gambar")
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val photoUri = output.savedUri ?: Uri.fromFile(photoFile)
                    val imageFile = uriToFile(photoUri, requireContext()).reduceFileImage()

                    val requestBody = user_id.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "face_photo",
                        imageFile.name,
                        requestImageFile
                    )

                    lifecycleScope.launch {
                        viewModel.verification(multipartBody, requestBody).observe(requireActivity()) {
                            when (it) {
                                is ResultState.Success -> {
                                    showLoading(false)
                                    val mBundle = Bundle()
                                    mBundle.putString("result", "true")
                                    findNavController().navigate(R.id.action_cameraFragment_to_emergencyResultFragment, mBundle)
                                }
                                is ResultState.Loading -> { showLoading(true) }
                                is ResultState.Error -> {
                                    showLoading(false)
                                    val mBundle = Bundle()
                                    mBundle.putString("result", "false")
                                    findNavController().navigate(R.id.action_cameraFragment_to_emergencyResultFragment, mBundle)                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        )
    }

    private fun getToken() {
        viewModel.getSession().observe(requireActivity()) {
            lifecycleScope.launch {
                viewModel.getUser(it.token).observe(requireActivity()) {
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        private const val TAG = "CameraFragment"
    }
}