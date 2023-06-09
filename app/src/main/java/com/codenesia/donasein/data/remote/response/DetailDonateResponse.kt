package com.codenesia.donasein.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailDonateResponse(

    @field:SerializedName("data")
    val data: DataDonate? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable

@Parcelize
data class DataDonate(

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

    @field:SerializedName("user")
    val user: UserDonate? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Parcelable

@Parcelize
data class UserDonate(

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("photo")
    val photo: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("fullname")
    val fullname: String? = null,

    @field:SerializedName("email")
    val email: String? = null
) : Parcelable

