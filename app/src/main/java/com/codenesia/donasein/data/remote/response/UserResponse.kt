package com.codenesia.donasein.data.remote.response

import android.net.Uri
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse (
    @field:SerializedName("userId")
    var userId: String? = null,

    @field:SerializedName("tokenId")
    var tokenId: String? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("email")
    var email: String? = null,

    @field:SerializedName("pass")
    var pass: String? = null,

    @field:SerializedName("image")
    var image: String? = null,
) : Parcelable

@Parcelize
data class RegisterResponse(

    @field:SerializedName("data")
    val data: List<RegisterItem>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable

@Parcelize
data class RegisterItem(

    @field:SerializedName("kk")
    val kk: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("rekening")
    val rekening: String? = null,

    @field:SerializedName("roles")
    val roles: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("fullname")
    val fullname: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("token")
    val token: String? = null
) : Parcelable

@Parcelize
data class LogoutResponse(

    @field:SerializedName("data")
    val data: DataLogout? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable

@Parcelize
data class DataLogout(

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("token")
    val token: String? = null
) : Parcelable

@Parcelize
data class LoginGoogleResponse(

    @field:SerializedName("data")
    val data: DataLoginGoogle? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable
@Parcelize
data class DataLoginGoogle(

    @field:SerializedName("fullname")
    val fullname: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("token")
    val token: String? = null
) : Parcelable

@Parcelize
data class ProfileResponse(

    @field:SerializedName("data")
    val data: ProfileData? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable

@Parcelize
data class ProfileData(

    @field:SerializedName("kk")
    var kk: String? = null,

    @field:SerializedName("address")
    var address: String? = null,

    @field:SerializedName("phone")
    var phone: String? = null,

    @field:SerializedName("rekening")
    var rekening: String? = null,

    @field:SerializedName("bank")
    var bank: String? = null,

    @field:SerializedName("gender")
    var gender: String? = null,

    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("fullname")
    var fullname: String? = null,

    @field:SerializedName("email")
    var email: String? = null
) : Parcelable


