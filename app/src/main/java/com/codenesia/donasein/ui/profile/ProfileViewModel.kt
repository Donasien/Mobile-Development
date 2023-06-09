package com.codenesia.donasein.ui.profile

import android.media.session.MediaSession.Token
import androidx.lifecycle.ViewModel
import com.codenesia.donasein.data.remote.response.ProfileData
import com.codenesia.donasein.data.repository.UserRepo

class ProfileViewModel(private val userRepo: UserRepo) : ViewModel() {

    fun getProfile(token: String) = userRepo.getProfile(token)

    fun updateProfile(token: String, data: ProfileData) = userRepo.updateProfile(token, data)

}