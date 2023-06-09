package com.codenesia.donasein.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.codenesia.donasein.data.Results
import com.codenesia.donasein.data.remote.network.ApiService
import com.codenesia.donasein.data.remote.response.*

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

    fun loginGoogleAction(email: String, fullname: String, token: String, photo: String): LiveData<Results<LoginGoogleResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.loginUserGoogle(email, fullname, token, photo)
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Get Login Google Action", e.message.toString())
            emit(Results.Error(e.message.toString()))
        }
    }
    fun loginAction(email: String, token: String): LiveData<Results<LogoutResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.loginUser(email, token)
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Get Login Action", e.message.toString())
            emit(Results.Error(e.message.toString()))
        }
    }

    fun logoutAction(token: String): LiveData<Results<LogoutResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.logoutUser(token)
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Get Logout Action", e.message.toString())
            emit(Results.Error(e.message.toString()))
        }
    }

    fun getProfile(token: String): LiveData<Results<ProfileResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.getProfile(token)
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Get Profile Action", e.message.toString())
            emit(Results.Error(e.message.toString()))
        }
    }

    fun updateProfile(
        token: String,
        data: ProfileData
    ) : LiveData<Results<ProfileResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.updateProfile(token, data.fullname.toString(), data.address.toString(), data.phone.toString(), data.kk.toString(), data.rekening.toString(), data.email.toString(), data.gender.toString(), data.bank.toString())
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Update Profile Action", e.message.toString())
            emit(Results.Error(e.message.toString()))
        }
    }

}