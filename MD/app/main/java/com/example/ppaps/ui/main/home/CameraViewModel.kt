package com.example.ppaps.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ppaps.data.Repository
import com.example.ppaps.data.pref.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CameraViewModel(private val repository: Repository) : ViewModel() {
    suspend fun verification(file: MultipartBody.Part, user_id: RequestBody) = repository.verification(file, user_id)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    suspend fun getUser(token: String) = repository.getUserData(token)
}