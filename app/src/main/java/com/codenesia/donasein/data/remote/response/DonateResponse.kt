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
