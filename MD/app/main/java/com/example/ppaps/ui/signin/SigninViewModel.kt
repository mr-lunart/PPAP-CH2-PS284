package com.example.ppaps.ui.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ppaps.data.Repository
import com.example.ppaps.data.pref.UserModel
import kotlinx.coroutines.launch

class SigninViewModel(private val repository: Repository) : ViewModel() {
    suspend fun login(username: String, password: String) = repository.login(username, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
        Log.d("saveSession", user.token)
    }
}