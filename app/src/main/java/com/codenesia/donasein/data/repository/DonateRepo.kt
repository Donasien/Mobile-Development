package com.codenesia.donasein.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.codenesia.donasein.data.Results
import com.codenesia.donasein.data.remote.network.ApiService
import com.codenesia.donasein.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    fun getDetailDonate(id: Int): LiveData<Results<DetailDonateResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.getDetailById(id)
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Get Detail Donate", e.message.toString())
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

    fun getPredictResult(token: RequestBody, file: MultipartBody.Part): LiveData<Results<MedicalApiResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.getPredictResult(token, file)
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Get Predict Result", e.message.toString())
            emit(Results.Error(e.message.toString()))
        }
    }

    fun submitDonation(
        token: RequestBody,
        title: RequestBody,
        target_amount: RequestBody,
        end_date: RequestBody,
        description: RequestBody,
        cover_photo: MultipartBody.Part,
        ktp_photo: MultipartBody.Part,
        medical_photo: MultipartBody.Part,
        disease_photo: MultipartBody.Part,
        sktm_photo: MultipartBody.Part,
        result_alzheimer: RequestBody,
        result_lung: RequestBody,
        type_disaster: RequestBody
    ) : LiveData<Results<SubmitDonationResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.submitDonation(
                token, title, target_amount, end_date, description, cover_photo, ktp_photo,
                medical_photo, disease_photo, sktm_photo, result_alzheimer, result_lung, type_disaster
            )
            emit(Results.Success(response))
        } catch (e: Exception) {
            Log.e("Submit Donation", e.message.toString())
            emit(Results.Error(e.message.toString()))
        }
    }
}