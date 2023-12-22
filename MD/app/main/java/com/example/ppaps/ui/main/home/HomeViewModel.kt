package com.example.ppaps.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ppaps.data.Repository
import com.example.ppaps.data.pref.UserModel
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    suspend fun getUser(token: String) = repository.getUserData(token)

    suspend fun checkVerif(userId: String) = repository.check(userId)
}