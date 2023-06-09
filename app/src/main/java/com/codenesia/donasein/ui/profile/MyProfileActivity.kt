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

        setDataProfile(user)

        binding.myProfileBtnEdit.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

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
        binding.myProfileTvFullname.text = data.fullname ?: userData.getUser().name
        binding.myProfileTvKk.text = data.kk ?: "Unavailable"
        binding.myProfileTvAddress.text = data.address ?: "Unavailable"
        binding.myProfileTvPhone.text = data.phone ?: "Unavailable"
        binding.myProfileTvEmail.text = data.email ?: "Unavailable"
        binding.myProfileTvRekening.text = data.rekening ?: "Unavailable"
        Glide.with(this)
            .load(userData.getUser().image ?: ResourcesCompat.getDrawable(resources, R.drawable.assets_preview_image, null))
            .into(binding.myProfileImgAccount)

        binding.loadingLottie.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}