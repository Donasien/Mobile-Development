package com.codenesia.donasein.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.codenesia.donasein.R
import com.codenesia.donasein.data.local.preference.UserPreferences
import com.codenesia.donasein.databinding.ActivitySplashBinding
import com.codenesia.donasein.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val user = UserPreferences(this)
        val u = user.getUser()


        Handler(Looper.getMainLooper()).postDelayed({
            if (u.name?.isEmpty() == true) {
                showExisting()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, DELAY_TIME)

    }

    private fun showExisting() {
        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }

    companion object {
        const val DELAY_TIME = 1500L
    }
}