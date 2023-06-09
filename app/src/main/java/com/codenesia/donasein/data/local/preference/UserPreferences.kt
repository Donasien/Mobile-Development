package com.codenesia.donasein.data.local.preference

import android.content.Context
import com.codenesia.donasein.data.remote.response.ProfileData
import com.codenesia.donasein.data.remote.response.UserResponse


class UserPreferences(context: Context) {
    companion object {
        private const val PREF_NAME = "user_preference"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val PASS = "pass"
        private const val IMAGE = "image"
        private const val KK = "kk"
        private const val PHONE = "phone"
        private const val ADDRESS = "address"
        private const val REKENING = "rekening"
        private const val GENDER = "gender"
        private const val BANK = "bank"
        private const val USER_ID = "user_id"
        private const val TOKEN_ID = "token_id"
    }

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setUser(user : UserResponse) {
        val editUser = preferences.edit()
        editUser.putString(NAME, user.name)
        editUser.putString(EMAIL, user.email)
        editUser.putString(PASS, user.pass)
        editUser.putString(IMAGE, user.image)
        editUser.putString(TOKEN_ID, user.tokenId)
        editUser.putString(USER_ID, user.userId)

        editUser.apply()
    }

    fun setProfile(user: UserResponse, address: String, kk: String, rekening: String, phone: String, gender: String, bank: String){
        val editUser = preferences.edit()
        editUser.putString(NAME, user.name)
        editUser.putString(EMAIL, user.email)
        editUser.putString(PASS, user.pass)
        editUser.putString(IMAGE, user.image)
        editUser.putString(TOKEN_ID, user.tokenId)
        editUser.putString(USER_ID, user.userId)
        editUser.putString(ADDRESS, address)
        editUser.putString(KK, kk)
        editUser.putString(REKENING, rekening)
        editUser.putString(PHONE, phone)
        editUser.putString(GENDER, gender)
        editUser.putString(BANK, bank)

        editUser.apply()
    }

    fun getUser(): UserResponse {
        val modelUser = UserResponse()
        modelUser.name = preferences.getString(NAME, "")
        modelUser.email = preferences.getString(EMAIL, "")
        modelUser.pass = preferences.getString(PASS, "")
        modelUser.image = preferences.getString(IMAGE, "")
        modelUser.tokenId = preferences.getString(TOKEN_ID, "")
        modelUser.userId = preferences.getString(USER_ID, "")

        return modelUser
    }

    fun getProfile(): ProfileData {
        val modelUser = ProfileData()
        modelUser.fullname = preferences.getString(NAME, "")
        modelUser.email = preferences.getString(EMAIL, "")
        modelUser.address = preferences.getString(ADDRESS, "")
        modelUser.kk = preferences.getString(KK, "")
        modelUser.rekening = preferences.getString(REKENING, "")
        modelUser.phone = preferences.getString(PHONE, "")
        modelUser

        return modelUser
    }
}