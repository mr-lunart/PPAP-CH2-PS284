package com.example.ppaps.ui.verification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ppaps.data.Repository
import com.example.ppaps.data.pref.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class VerificationViewModel(private val repository: Repository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    suspend fun getUser(token: String) = repository.getUserData(token)
    suspend fun upload(file: MultipartBody.Part, user_id: RequestBody) = repository.uploadImage(file, user_id)

    suspend fun confirm(userId: String) = repository.confirm(userId)
}