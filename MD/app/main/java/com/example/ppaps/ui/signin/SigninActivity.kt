package com.example.ppaps.ui.signin

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
import com.example.ppaps.R
import com.example.ppaps.data.ResultState
import com.example.ppaps.data.pref.UserModel
import com.example.ppaps.databinding.ActivitySigninBinding
import com.example.ppaps.ui.ViewModelFactory
import com.example.ppaps.ui.main.MainActivity
import com.example.ppaps.ui.signup.SignupViewModel
import com.example.ppaps.ui.verification.VerificationActivity
import kotlinx.coroutines.launch

class SigninActivity : AppCompatActivity() {
    private val viewModel by viewModels<SigninViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        loginAction()
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

    private fun loginAction() {
        binding.btnSignin.setOnClickListener {
            lifecycleScope.launch {
                val username = binding.usernameEditText.text.toString()
                val password = binding.pwEditText.text.toString()

                viewModel.login(username, password).observe(this@SigninActivity) {
                    when (it) {
                        is ResultState.Success -> {
                            showLoading(false)
                            viewModel.saveSession(UserModel(username, it.data.data?.token!!, true))

                            val intent = Intent(this@SigninActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        is ResultState.Loading -> showLoading(true)
                        is ResultState.Error -> {
                            showLoading(false)
                            AlertDialog.Builder(this@SigninActivity).apply {
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}