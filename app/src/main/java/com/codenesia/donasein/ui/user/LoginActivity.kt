package com.codenesia.donasein.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codenesia.donasein.databinding.ActivityLoginBinding
import com.codenesia.donasein.ui.splash.StartActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        supportActionBar?.hide()
        loginBinding.loginBtnBack.setOnClickListener { intentFunction("back") }
        loginBinding.loginTvCtaForgotPass.setOnClickListener { intentFunction("forgot_password") }
        loginBinding.loginTvCta2.setOnClickListener { intentFunction("register") }
        loginBinding.loginBtnSignIn.setOnClickListener { manualSign() }
        loginBinding.loginBtnSignInWithGoogle.setOnClickListener { googleSign() }
    }

    private fun googleSign() {
        TODO("Not yet implemented")
    }

    private fun manualSign() {
        TODO("Not yet implemented")
    }

    private fun intentFunction(s: String) {
        if (s == "back") {
            startActivity(Intent(this, StartActivity::class.java))
        } else if (s == "register") {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}