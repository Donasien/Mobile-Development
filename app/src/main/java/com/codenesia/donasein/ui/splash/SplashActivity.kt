package com.codenesia.donasein.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.codenesia.donasein.R
import com.codenesia.donasein.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Handler(Looper.getMainLooper()).postDelayed({
            showExisting()
        }, DELAY_TIME)

        supportActionBar?.hide()
    }

    private fun showExisting() {
        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }

    companion object {
        const val DELAY_TIME = 1500L
    }
}