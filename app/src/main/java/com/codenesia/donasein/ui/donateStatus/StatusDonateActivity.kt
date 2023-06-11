package com.codenesia.donasein.ui.donateStatus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.codenesia.donasein.databinding.ActivityStatusDonateBinding

class StatusDonateActivity : AppCompatActivity() {

    private lateinit var binds: ActivityStatusDonateBinding

    companion object {
        const val RESULT_STATUS_DONATE = "result_status_donate"
        var result_status_donate = String()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binds = ActivityStatusDonateBinding.inflate(layoutInflater)
        setContentView(binds.root)

        result_status_donate = intent.getStringExtra(RESULT_STATUS_DONATE).toString()

        setView(result_status_donate)
        Log.e("Stts", result_status_donate.toString())

    }

    private fun setView(res: String) {
        if (res == "pending") {
            binds.statusPending.visibility = View.VISIBLE
        } else if(res == "accept") {
            binds.statusAccept.visibility = View.VISIBLE
        } else if(res == "decline") {
            binds.statusRejected.visibility = View.VISIBLE
        } else {
            binds.statusEmpty.visibility = View.VISIBLE
        }
    }
}