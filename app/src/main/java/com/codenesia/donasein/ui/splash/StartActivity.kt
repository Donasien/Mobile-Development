package com.codenesia.donasein.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.codenesia.donasein.data.local.preference.UserPreferences
import com.codenesia.donasein.databinding.ActivityStartBinding
import com.codenesia.donasein.ui.main.MainActivity
import com.codenesia.donasein.ui.user.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.stscBtnAnonym.setOnClickListener { intentTo(1) }
        binding.stscBtnLogin.setOnClickListener { intentTo(2) }
        supportActionBar?.hide()

    }

    private fun intentTo(i: Int) {
        if (i == 1) {
            startActivity(Intent(this@StartActivity, MainActivity::class.java))
            finish()
        } else if (i == 2) {
            startActivity(Intent(this@StartActivity, LoginActivity::class.java))
        }
    }
}