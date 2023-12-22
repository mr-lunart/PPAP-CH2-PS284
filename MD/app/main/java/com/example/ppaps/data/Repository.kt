package com.example.ppaps.data

import android.util.Log
import androidx.lifecycle.liveData
import com.example.ppaps.data.pref.UserModel
import com.example.ppaps.data.pref.UserPreference
import com.example.ppaps.data.remote.ApiService
import com.example.ppaps.data.response.CheckResponse
import com.example.ppaps.data.response.EmergencyVerifResponse
import com.example.ppaps.data.response.ListHospitalResponse
import com.example.ppaps.data.response.LoginResponse
import com.example.ppaps.data.response.Response
import com.example.ppaps.data.response.UserResponse
import com.example.ppaps.data.response.VerificationResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException

class Repository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    ) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(username: String, password: String, nohp: String,name: String, ) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.register(username, password, nohp, name)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, Response::class.java)
            Log.e("Repository", errorBody.message.toString())
            emit(errorBody.message?.let { ResultState.Error(it) })
        }
    }

    suspend fun login(username: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.login(username, password)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            emit(errorBody.message?.let { ResultState.Error(it) })
        }
    }

    suspend fun getHospital(token: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.listHospitals("Bearer $token")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ListHospitalResponse::class.java)
            emit(errorBody.message?.let { ResultState.Error(it) })
        }
    }

    suspend fun getUserData(token: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getUser("Bearer $token")
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            emit(errorBody.message?.let { ResultState.Error(it) })
        }
    }

    suspend fun confirm(userId: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.confirmation("Bearer secret", "https://registration-a56t3srpta-uc.a.run.app/confirm",  userId)
            when (response.data) {
                1 -> {
                    emit(ResultState.Success(response))
                }
                0, -1 -> {
                    emit(ResultState.Error(response.status?.message))
                }
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, VerificationResponse::class.java)
            emit(errorBody.status?.message?.let { ResultState.Error(it) })
        }
    }

    suspend fun check(userId: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.check("Bearer secret", "https://registration-a56t3srpta-uc.a.run.app/check",  userId)
            when (response.accountStatus) {
                1 -> {
                    emit(ResultState.Success(response))
                }
                2 -> {
                    emit(response.status?.message?.let { ResultState.Error(it) })
                }
                0, -1 -> {
                    emit(response.status?.message?.let { ResultState.Error(it) })
                }
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, CheckResponse::class.java)
            emit(errorBody.status?.message?.let { ResultState.Error(it) })
        }
    }

    suspend fun uploadImage(photo: MultipartBody.Part, user_id: RequestBody) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.upload("Bearer secret", "https://registration-a56t3srpta-uc.a.run.app/upload",  photo, user_id)
            when (response.data) {
                1 -> {
                    emit(ResultState.Success(response))
                }
                0, null -> {
                    emit(ResultState.Error(response.status?.message))
                }
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, VerificationResponse::class.java)
            emit(errorBody.status?.message?.let { ResultState.Error(it) })
        } catch (e: SocketTimeoutException) {
            emit(ResultState.Error(e.message))
        }
    }

    suspend fun verification(photo: MultipartBody.Part, user_id: RequestBody) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.verification("Bearer secret", "https://verification-a56t3srpta-uc.a.run.app/verification",  photo, user_id)
            when (response.data) {
                1 -> {
                    emit(ResultState.Success(response))
                }
                -1, 0, 2 -> {
                    Log.d("TAG", "verification: ${response.status?.message}")
                    emit(ResultState.Error(response.status?.message))
                }
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, EmergencyVerifResponse::class.java)
            emit(errorBody.status?.message?.let { ResultState.Error(it) })
            Log.d("TAG", "verification: ${errorBody.status?.message}")
        } catch (e: SocketTimeoutException) {
            emit(ResultState.Error(e.message))
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(userPreference: UserPreference, apiService: ApiService): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService)
            }.also { instance = it }
    }
}