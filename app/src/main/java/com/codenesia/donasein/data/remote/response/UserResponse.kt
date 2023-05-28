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
data class RegisterItems(
    @field:SerializedName("password")
    val token: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("name")
    val name: String? = null
) : Parcelable

@Parcelize
data class RegisterResponse(
    @field:SerializedName("registerItems")
    val registerItems: RegisterItems,

    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable
