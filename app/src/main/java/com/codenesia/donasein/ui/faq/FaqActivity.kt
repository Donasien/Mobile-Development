package com.codenesia.donasein.ui.faq

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codenesia.donasein.databinding.ActivityFaqBinding

class FaqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.faqBtnBack.setOnClickListener {
            onBackPressed()
        }

        with(binding) {
            expandable1.setOnExpandListener {
                if (it) {
                    expandable1.spinnerColor = Color.BLACK
                } else {
                    expandable1.spinnerColor = Color.GREEN
                }
            }
            expandable1.parentLayout.setOnClickListener { expandable1.toggleLayout() }

            expandable2.setOnExpandListener {
                if (it) {
                    expandable2.spinnerColor = Color.BLACK
                } else {
                    expandable2.spinnerColor = Color.GREEN
                }
            }
            expandable2.parentLayout.setOnClickListener { expandable2.toggleLayout() }

            expandable3.setOnExpandListener {
                if (it) {
                    expandable3.spinnerColor = Color.BLACK
                } else {
                    expandable3.spinnerColor = Color.GREEN
                }
            }
            expandable3.parentLayout.setOnClickListener { expandable3.toggleLayout() }
        }
    }
}