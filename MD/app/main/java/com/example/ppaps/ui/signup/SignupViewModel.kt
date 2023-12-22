package com.example.ppaps.ui.signup

import androidx.lifecycle.ViewModel
import com.example.ppaps.data.Repository

class SignupViewModel(private val repository: Repository) : ViewModel() {
    suspend fun register(username: String, password: String, nohp: String,name: String) = repository.register(username, password, nohp, name)
}