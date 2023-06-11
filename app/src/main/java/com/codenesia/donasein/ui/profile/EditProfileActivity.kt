package com.codenesia.donasein.ui.profile

import android.content.Intent
import android.media.session.MediaSession.Token
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import com.codenesia.donasein.R
import com.codenesia.donasein.data.Results
import com.codenesia.donasein.data.local.preference.UserPreferences
import com.codenesia.donasein.data.remote.response.ProfileData
import com.codenesia.donasein.databinding.ActivityEditProfileBinding
import com.codenesia.donasein.ui.ViewModelFactory
import com.codenesia.donasein.ui.main.MainActivity
import com.codenesia.donasein.ui.main.ui.profile.ProfileFragment
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory(this)
    }
    private var bankType: String? = null
    private var genderType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editProfileEtxTypebank.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            bankType = newText
        }

        binding.editProfileEtxGender.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            genderType = newText
        }

        binding.editProfileBtnBack.setOnClickListener {
            onBackPressed()
        }

        binding.editProfileBtnSave.setOnClickListener {
            saveAction()
        }

        setEditProfile()
    }

    private fun saveAction() {
        val userPref = UserPreferences(this)
        val user = userPref.getProfile()

        // fullname edit text
        val _fullname = binding.editProfileEtxFullname.text.toString()
        var fullname = if (_fullname.isEmpty()) user.fullname else _fullname

        // address edit text
        val _address = binding.editProfileEtxAddress.text.toString()
        var address = if (_address.isEmpty()) user.address else _address

        // phone edit text
        val _phone = binding.editProfileEtxPhone.text.toString()
        var phone = if (_phone.isEmpty()) user.phone else _phone

        // kk edit text
        val _kk = binding.editProfileEtxKk.text.toString()
        var kk = if (_kk.isEmpty()) user.kk else _kk

        // email edit text
        val _email = binding.editProfileEtxEmail.text.toString()
        var email = if (_email.isEmpty()) user.email else _email

        val _rekening = binding.editProfileEtxRekening.text.toString()
        var rekening = if (_rekening.isEmpty()) user.rekening else _rekening

        bindingProfile(fullname, address, phone, kk, email, rekening, userPref.getUser().tokenId.toString(), genderType, bankType)

    }

    private fun bindingProfile(fullname: String?, address: String?, phone: String?, kk: String?, email: String?, rekening: String?, token: String?, gender: String?, bank: String?) {
        val data = ProfileData()
        data.email = email
        data.address = address
        data.phone = phone
        data.rekening = rekening
        data.fullname = fullname
        data.kk = kk
        data.gender = gender
        data.bank = bank

        profileViewModel.updateProfile(token.toString(), data).observe(this){ result ->
            if (result!=null) {
                when(result){
                    is Results.Error -> {
                        binding.loadingLottie.visibility = View.GONE
                        Log.e("Edit Profile", result.error.toString())
                        showMessage(false, "Update Profile Failed")
                    }
                    is Results.Success -> {
                        binding.loadingLottie.visibility = View.GONE
                        val dataUser = result.data.data
                        val userPref = UserPreferences(this)
                        val userDataPref = userPref.getUser()
                        showMessage(true, "Update Profile Success")
                        userPref.setProfile(userDataPref, dataUser?.address.toString(), dataUser?.kk.toString(), dataUser?.rekening.toString(), dataUser?.phone.toString(), data.gender.toString(), data.bank.toString())
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    is Results.Loading -> {
                        binding.loadingLottie.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setEditProfile() {
        val user = UserPreferences(this)
        profileViewModel.getProfile(user.getUser().tokenId.toString()).observe(this){ result ->
            if (result!=null) {
                when(result){
                    is Results.Error -> {
                        binding.loadingLottie.visibility = View.GONE
                        Log.e("Edit Profile", result.error.toString())
                    }
                    is Results.Success -> {
                        val data = result.data.data
                        if (data != null) {
                            binding.loadingLottie.visibility = View.GONE
                            setDataEditProfile(data)
                            val userPref = UserPreferences(this)
                            val userDataPref = userPref.getUser()
                            userPref.setProfile(userDataPref, data.address.toString(), data.kk.toString(), data.rekening.toString(), data.phone.toString(), data.gender.toString(), data.bank.toString())

                        }
                    }
                    is Results.Loading -> {
                        binding.loadingLottie.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setDataEditProfile(data: ProfileData) {
        binding.editProfileEtxAddress.hint = data.address ?: "Enter your address"
        binding.editProfileEtxFullname.hint = data.fullname ?: "Enter your fullname"
        binding.editProfileEtxPhone.hint = data.phone ?: "Enter your phone"
        binding.editProfileEtxEmail.hint = data.email ?: "Enter your email"
        binding.editProfileEtxRekening.hint = data.rekening ?: "Enter your rekening"
        binding.editProfileEtxKk.hint = data.kk ?: "Enter your family id"
        binding.editProfileEtxGender.hint = data.gender ?: "Select your gender"
        binding.editProfileEtxTypebank.hint = data.bank ?: "Select yout bank"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun showMessage(check: Boolean, message: String) {
        if (check) {
            MotionToast.createColorToast(
                this,
                "Success",
                message,
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, R.font.poppins_medium)
            )
        } else {
            MotionToast.createColorToast(
                this,
                "Failed",
                message,
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, R.font.poppins_medium)
            )
        }
    }

}