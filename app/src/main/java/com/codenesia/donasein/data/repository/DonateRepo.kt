package com.codenesia.donasein.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.codenesia.donasein.data.Results
import com.codenesia.donasein.data.remote.network.ApiService
import com.codenesia.donasein.data.remote.response.DonateResponse
import com.codenesia.donasein.data.remote.response.NewsHealthResponse

class DonateRepo(private val apiService: ApiService) {
    fun getAllDonate(): LiveData<Results<DonateResponse>> = liveData{
        emit(Results.Loading)
        try {
            val response = apiService.getAllDonation()
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Get All Donate", e.message.toString())
            emit(Results.Error(e.message.toString()))
        }
    }

    fun getNewsHealth(): LiveData<Results<NewsHealthResponse>> = liveData{
        emit(Results.Loading)
        try {
            val response = apiService.getNewsHealth("us", "health", "84da10b4d6c649ec807fcbf066be028d")
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Get News Health", e.message.toString())
            emit(Results.Error(e.message.toString()))
        }
    }
}