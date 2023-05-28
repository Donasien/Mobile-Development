package com.codenesia.donasein.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import com.codenesia.donasein.R
import com.codenesia.donasein.data.local.preference.UserPreferences
import com.codenesia.donasein.data.remote.response.UserResponse
import com.codenesia.donasein.databinding.ActivityRegisterBinding
import com.codenesia.donasein.ui.main.MainActivity
import com.codenesia.donasein.ui.splash.StartActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        auth = Firebase.auth

        supportActionBar?.hide()
        registerBinding.registerTvCta2.setOnClickListener { intentToLogin() }
        registerBinding.registerBtnSignUp.setOnClickListener { signUp() }
        registerBinding.registerBtnBack.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }
    }

    private fun signUp() {
        val username = registerBinding.registerEtxUsername.text.toString()
        val email = registerBinding.registerEtxEmail.text.toString()
        val pass = registerBinding.registerEtxPass.text.toString()
        if (username.isEmpty()){
            registerBinding.registerEtUsername.error = "Username tidak valid"
        } else {
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                registerBinding.registerEtEmail.error = "Email tidak valid"

            } else {
                if (pass.length < 8) {
                    registerBinding.registerEtPass.error = "Password minimal 8 karakter"
                } else {
                    createAccount(username, email, pass)
                }
            }
        }
    }

    private fun updateUsername(username: String) {
        val user = auth.currentUser
        val userUpdate = userProfileChangeRequest {
            displayName = username
        }

        user!!.updateProfile(userUpdate).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                setDataUser()
                showLoading(false)
                showMessage(true, "Register Success")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
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


    private fun createAccount(username: String, email: String, pass: String) {
        try {
            showLoading(true)
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    updateUsername(username)
                } else {
                    showLoading(false)
                    showMessage(false, "Failed Sign Up User")
                }
            }
        } catch (e: Exception) {
            showMessage(false, "Failed Sign Up User")
            Log.e("Register", e.message.toString())
        }
    }

    private fun intentToLogin() {
        startActivity(Intent(this, LoginActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
    }

    private fun showLoading(check: Boolean) {
        if (check) {
            registerBinding.loadingLottie.visibility = View.VISIBLE
        } else {
            registerBinding.loadingLottie.visibility = View.GONE
        }
    }

    private fun setDataUser(){
        val data = UserResponse()
        val dataFromFb = auth.currentUser
        if (dataFromFb != null) {
            data.let {
                it.userId = dataFromFb.uid
                it.name = dataFromFb.displayName
                it.email = dataFromFb.email
                it.image= dataFromFb.photoUrl.toString()
                it.userId = dataFromFb.uid
                it.tokenId= dataFromFb.getIdToken(false).result.token.toString()
            }
            val user = UserPreferences(this)
            user.setUser(data)
        }
    }

}