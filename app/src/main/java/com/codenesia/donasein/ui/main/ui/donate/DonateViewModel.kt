package com.codenesia.donasein.ui.main.ui.donate

import androidx.lifecycle.ViewModel
import com.codenesia.donasein.data.repository.DonateRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DonateViewModel(private val donateRepo: DonateRepo) : ViewModel() {

    fun getAllDonate() = donateRepo.getAllDonate()
    fun getDetailDonateById(id: Int) = donateRepo.getDetailDonate(id)

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
    ) = donateRepo.submitDonation(token, title, target_amount, end_date, description, cover_photo, ktp_photo, medical_photo, disease_photo, sktm_photo, result_alzheimer, result_lung, type_disaster)
}

class NewsViewModel(private val donateRepo: DonateRepo): ViewModel() {
    fun getNewsHealth() = donateRepo.getNewsHealth()

}