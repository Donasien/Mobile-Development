package com.codenesia.donasein.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.codenesia.donasein.data.Results
import com.codenesia.donasein.data.remote.network.ApiService
import com.codenesia.donasein.data.remote.response.NewsHealthResponse
import com.codenesia.donasein.data.remote.response.RegisterResponse

class UserRepo(private val apiService: ApiService) {

    fun registerAction(email: String, fullname: String, token: String): LiveData<Results<RegisterResponse>> = liveData{
        emit(Results.Loading)
        try {
            val response = apiService.registerUser(email, fullname, token)
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Get Register Action", e.message.toString())
            emit(Results.Error(e.message.toString()))
        }
    }

}