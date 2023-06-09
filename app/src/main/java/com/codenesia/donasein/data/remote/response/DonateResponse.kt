package com.codenesia.donasein.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DonateResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataItem(

	@field:SerializedName("end_date")
	val endDate: String? = null,

	@field:SerializedName("latest_amount")
	val latestAmount: Int? = null,

	@field:SerializedName("target_amount")
	val targetAmount: Int? = null,

	@field:SerializedName("cover_photo")
	val coverPhoto: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("days_left")
	val daysLeft: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable


@Parcelize
data class SubmitDonationResponse(

	@field:SerializedName("data")
	val data: SubmitData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class SubmitData(

	@field:SerializedName("kk")
	val kk: String? = null,

	@field:SerializedName("end_date")
	val endDate: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("target_amount")
	val targetAmount: String? = null,

	@field:SerializedName("cover_photo")
	val coverPhoto: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("house_photo")
	val housePhoto: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("disease_photo")
	val diseasePhoto: String? = null,

	@field:SerializedName("ktp_photo")
	val ktpPhoto: String? = null,

	@field:SerializedName("medical_photo")
	val medicalPhoto: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
) : Parcelable

@Parcelize
data class MedicalApiResponse(
	@field:SerializedName("prediction_alzheimer")
	val predictionAlzheimer: String? = null,

	@field:SerializedName("prediction_lung")
	val predictionLung: String? = null
) : Parcelable

