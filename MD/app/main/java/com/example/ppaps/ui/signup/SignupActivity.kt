package com.example.ppaps.ui.signup

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.ppaps.data.ResultState
import com.example.ppaps.databinding.ActivitySignupBinding
import com.example.ppaps.ui.ViewModelFactory
import com.example.ppaps.ui.main.MainActivity
import com.example.ppaps.ui.signin.SigninActivity
import com.example.ppaps.ui.verification.VerificationActivity
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        initAction()
        registerAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun initAction() {
        binding.apply {
            btnSignin.setOnClickListener {
                val intent = Intent(this@SignupActivity, SigninActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun registerAction() {
        binding.btnSignup.setOnClickListener {
            lifecycleScope.launch {
                val username = binding.emailEditText.text.toString()
                val password = binding.pwEditText.text.toString()
                val nohp = binding.hpEditText.text.toString()
                val name = binding.nameEditText.text.toString()
                if (username.isEmpty() || password.isEmpty() || nohp.isEmpty() || name.isEmpty()) {
                    AlertDialog.Builder(this@SignupActivity).apply {
                        setTitle("Error")
                        setMessage("Data tidak boleh kosong")
                        setPositiveButton("Lanjut") { _, _ -> }
                        create()
                        show()
                    }
                } else {
                    viewModel.register(username, password, nohp, name).observe(this@SignupActivity) {
                        when (it) {
                            is ResultState.Success -> {
                                showLoading(false)
                                AlertDialog.Builder(this@SignupActivity).apply {
                                    setTitle("Akun terbuat!!!")
                                    setMessage("Selamat akun dengan username $username telah terbuat, silahkan login terlebih dahulu")
                                    setPositiveButton("Lanjut") { _, _ ->
                                        val intent =
                                            Intent(this@SignupActivity, VerificationActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                    }
                                    create()
                                    show()
                                }
                            }
                            is ResultState.Loading -> showLoading(true)
                            is ResultState.Error -> {
                                showLoading(false)
                                AlertDialog.Builder(this@SignupActivity).apply {
                                    setTitle("Error")
                                    setMessage(it.message)
                                    setPositiveButton("Lanjut") { _, _ -> }
                                    create()
                                    show()
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}