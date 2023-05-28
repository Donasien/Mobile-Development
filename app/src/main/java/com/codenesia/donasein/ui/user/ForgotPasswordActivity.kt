package com.codenesia.donasein.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.codenesia.donasein.R
import com.codenesia.donasein.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        binding.fpBtnBack.setOnClickListener { intentBack() }
        binding.fpBtnSend.setOnClickListener { sendVerification() }

        supportActionBar?.hide()
    }

    private fun sendVerification() {
        val email = binding.fpEtxEmail.text.toString()
        val edtEmail = binding.fpEtEmail

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.error = "Masukkan Email"
            edtEmail.requestFocus()
        } else {
            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isComplete) {
                    showMessage(true, "Send Verification Success")
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    showMessage(false, "Verification Failed")
                }
            }
        }
    }


    private fun intentBack() {
        startActivity(Intent(this, LoginActivity::class.java))
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