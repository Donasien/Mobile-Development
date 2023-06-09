package com.codenesia.donasein.ui.aboutUs

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codenesia.donasein.R
import com.codenesia.donasein.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.aboutBtnBack.setOnClickListener {
            onBackPressed()
        }

        with(binding) {
            expandable.setOnExpandListener {
                if (it) {
                    expandable.spinnerColor = Color.BLACK
                } else {
                    expandable.spinnerColor = Color.GREEN
                }
            }
            expandable.parentLayout.setOnClickListener { expandable.toggleLayout() }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}