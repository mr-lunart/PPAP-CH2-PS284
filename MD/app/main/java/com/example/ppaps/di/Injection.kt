package com.example.ppaps.di

import android.content.Context
import com.example.ppaps.data.Repository
import com.example.ppaps.data.pref.UserPreference
import com.example.ppaps.data.pref.dataStore
import com.example.ppaps.data.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(pref, apiService)
    }
}