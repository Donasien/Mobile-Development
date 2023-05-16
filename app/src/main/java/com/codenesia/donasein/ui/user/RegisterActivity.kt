package com.codenesia.donasein.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codenesia.donasein.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        supportActionBar?.hide()
        registerBinding.registerTvCta2.setOnClickListener { intentToLogin() }
    }

    private fun intentToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}