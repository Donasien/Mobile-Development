package com.codenesia.donasein.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.codenesia.donasein.R
import com.codenesia.donasein.data.Results
import com.codenesia.donasein.data.local.preference.UserPreferences
import com.codenesia.donasein.data.remote.response.ProfileData
import com.codenesia.donasein.databinding.ActivityMyProfileBinding
import com.codenesia.donasein.ui.ViewModelFactory
import com.codenesia.donasein.ui.user.UserViewModel

class MyProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myProfileBtnBack.setOnClickListener {
            onBackPressed()
        }

        val user = UserPreferences(this)

        if (user.getProfile().address!!.isEmpty()) {
            setDataProfile(user)
        } else {
            bindingData(user)
        }

        binding.myProfileBtnEdit.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
            finish()
        }

    }

    private fun bindingData(user: UserPreferences) {
        val dataUser = user.getUser()
        val dataProfile = user.getProfile()
        binding.myProfileTvKk.text = if(dataProfile.kk != null && dataProfile.kk != "null") dataProfile.kk else "Data is not yet"
        binding.myProfileTvEmail.text = dataUser.email
        binding.myProfileTvRekening.text = if(dataProfile.rekening != null && dataProfile.rekening != "null") dataProfile.rekening else "Data is not yet"
        binding.myProfileTvAddress.text = if(dataProfile.address != null && dataProfile.address != "null") dataProfile.address else "Data is not yet"
        binding.myProfileTvPhone.text = if(dataProfile.phone != null && dataProfile.phone != "null") dataProfile.phone else "Data is not yet"
        Glide.with(this)
            .load(dataUser.image ?: ResourcesCompat.getDrawable(resources, R.drawable.assets_preview_image, null))
            .into(binding.myProfileImgAccount)
    }

    private fun setDataProfile(user: UserPreferences) {
        profileViewModel.getProfile(user.getUser().tokenId.toString()).observe(this) { result ->
            if (result!=null){
                when(result){
                    is Results.Error -> {

                    }
                    is Results.Success -> {
                        val data = result.data.data
                        if (data != null) {
                            setProfile(data)
                        }
                    }
                    is Results.Loading -> {
                        binding.loadingLottie.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setProfile(data: ProfileData) {
        val userData = UserPreferences(this)
        binding.loadingLottie.visibility = View.GONE
        userData.setProfile(
            userData.getUser(), data.address.toString(), data.kk.toString(),
            data.rekening.toString(), data.phone.toString(), "", "")
        bindingData(userData)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}