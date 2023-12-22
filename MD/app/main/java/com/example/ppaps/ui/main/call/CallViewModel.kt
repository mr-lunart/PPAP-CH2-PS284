package com.example.ppaps.ui.main.call

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ppaps.data.Repository
import com.example.ppaps.data.pref.UserModel

class CallViewModel(private val repository: Repository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    suspend fun getHospital(token: String) = repository.getHospital(token)
}