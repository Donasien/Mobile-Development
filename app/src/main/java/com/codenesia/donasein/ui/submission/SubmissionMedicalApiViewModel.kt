package com.codenesia.donasein.ui.submission

import androidx.lifecycle.ViewModel
import com.codenesia.donasein.data.repository.DonateRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SubmissionMedicalApiViewModel(private val donateRepo: DonateRepo) : ViewModel() {
    fun getPredictResult(token: RequestBody, file:MultipartBody.Part) = donateRepo.getPredictResult(token, file)
}