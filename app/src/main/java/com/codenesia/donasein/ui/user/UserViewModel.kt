package com.codenesia.donasein.ui.user

import androidx.lifecycle.ViewModel
import com.codenesia.donasein.data.repository.UserRepo

class UserViewModel(private val userRepo: UserRepo) : ViewModel() {
    fun getRegisterResponse(email: String, fullname: String, token: String) = userRepo.registerAction(email, fullname, token)
    fun getLogoutResponse(token: String) = userRepo.logoutAction(token)
    fun getLoginResponse(email: String, token: String) = userRepo.loginAction(email, token)
    fun getLoginGoogleResponse(email: String, fullname: String, token: String, photo: String) = userRepo.loginGoogleAction(email, fullname, token, photo)


}