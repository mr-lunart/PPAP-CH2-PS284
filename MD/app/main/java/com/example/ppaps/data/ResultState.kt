package com.example.ppaps.data

sealed class ResultState<out R> private constructor(){
    data class Success<out T>(val data: T): ResultState<T>()
    data class Error(val message: String? = null): ResultState<Nothing>()
    object Loading: ResultState<Nothing>()
}