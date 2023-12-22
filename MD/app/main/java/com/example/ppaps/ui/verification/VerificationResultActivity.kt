package com.example.ppaps.ui.verification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.ppaps.R
import com.example.ppaps.databinding.ActivityVerificationResultBinding
import com.example.ppaps.ui.signin.SigninActivity

class VerificationResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationResultBinding
    private val handler = Handler(Looper.getMainLooper())
    private val delay: Long = 3000
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.face_verif)

        val result = intent.getStringExtra("result")
        if (result == "true") {
            binding.ivResult.setImageResource(R.drawable.baseline_check_circle_outline_24)
            binding.tvResult.text = getString(R.string.verification_success)

            runnable = object : Runnable {
                override fun run() {
                    changeActivity()
                    handler.postDelayed(this, delay)
                }
            }

            handler.postDelayed(runnable, delay)
        }
    }

    private fun changeActivity() {
        val intent = Intent(this, SigninActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}