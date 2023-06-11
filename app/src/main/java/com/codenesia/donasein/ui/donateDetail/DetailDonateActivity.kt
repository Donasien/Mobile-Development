package com.codenesia.donasein.ui.donateDetail

import android.content.Intent
import android.icu.text.NumberFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.codenesia.donasein.data.Results
import com.codenesia.donasein.data.remote.response.DataDonate
import com.codenesia.donasein.databinding.ActivityDetailDonateBinding
import com.codenesia.donasein.ui.ViewModelFactory
import com.codenesia.donasein.ui.main.ui.donate.DonateViewModel
import com.codenesia.donasein.ui.payment.PaymentActivity
import java.util.*

class DetailDonateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDonateBinding
    private val donateViewModel: DonateViewModel by viewModels {
        ViewModelFactory(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailDonateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id_donate = intent.getStringExtra(ID_DONATE).toString()
        if (id_donate.isNotEmpty()) {
            bindData(id_donate.toInt())
        }

        binding.detailDonateBtnBack.setOnClickListener {
            onBackPressed()
        }

        binding.detailDonateBtnDonate.setOnClickListener {
            val donate = binding.detailDonateEtxDonate.text.toString()

            if (donate.isEmpty()) {
                binding.detailDonateEtDonate.error = "Masukkan jumlah donasi"
            } else {
                if (donate.length > remainingAmount.length){
                    binding.detailDonateEtDonate.error = "Jumlah donasi melebihi target sisa"
                } else {
                    if (donate.toInt() > remainingAmount.toInt()) {
                        binding.detailDonateEtDonate.error = "Jumlah donasi melebihi target sisa"
                    } else {
                        val intent = Intent(this, PaymentActivity::class.java)
                        intent.putExtra(PaymentActivity.TARGET_AMOUNT, targetAmount)
                        intent.putExtra(PaymentActivity.TITLE_DONATION, titleDonation)
                        intent.putExtra(PaymentActivity.REMAINING_AMOUNT, remainingAmount)
                        donation = donate
                        intent.putExtra(PaymentActivity.DONATION, donation)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun bindData(id: Int) {
        donateViewModel.getDetailDonateById(id).observe(this){ result ->
            if (result != null) {
                when(result) {
                    is Results.Error -> { Log.e("error", result.error.toString())}
                    is Results.Loading -> {}
                    is Results.Success -> {
                        val data = result.data.data

                        if (data != null) {
                            bindView(data)
                        }
                    }
                }
            }
        }
    }

    private fun bindView(data: DataDonate) {
        val localeID : Locale = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)

        binding.detailDonateTvTitle.text = data.title
        binding.detailDonateTvDesc.text = data.description
        binding.detailDonateTvDays.text = data.daysLeft.toString().plus(" days left")
        val target = formatRupiah.format(data.targetAmount?.toDouble())
        val latest = formatRupiah.format(data.latestAmount?.toDouble())
        binding.detailDonateTvLatest.text = latest.toString()
        binding.detailDonateTvTarget.text = target.toString()
        binding.animatedProgressBar.setMax(data.targetAmount!!)
        binding.animatedProgressBar.setProgress(data.latestAmount!!)
        binding.detailDonateTvUsername.text = data.user?.fullname
        Glide.with(this).load(data.coverPhoto).into(binding.detailDonateImgDonate)
        Glide.with(this).load(data.user?.photo).into(binding.detailDonateImgAccount)

        targetAmount = data.targetAmount.toString()
        remainingAmount = (data.targetAmount - data.latestAmount).toString()
        titleDonation = data.title.toString()


    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    companion object {
        const val ID_DONATE = "id_donate"
        var id_donate = String()
        var targetAmount = String()
        var titleDonation = String()
        var remainingAmount = String()
        var donation = String()
    }
}