package com.codenesia.donasein.ui.user

import androidx.lifecycle.ViewModel
import com.codenesia.donasein.data.repository.UserRepo

class UserViewModel(private val userRepo: UserRepo) : ViewModel() {
    fun getRegisterResponse(email: String, fullname: String, token: String) = userRepo.registerAction(email, fullname, token)
}