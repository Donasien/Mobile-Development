package com.codenesia.donasein.ui.main.ui.donate

import androidx.lifecycle.ViewModel
import com.codenesia.donasein.data.repository.DonateRepo

class DonateViewModel(private val donateRepo: DonateRepo) : ViewModel() {

    fun getAllDonate() = donateRepo.getAllDonate()
}

class NewsViewModel(private val donateRepo: DonateRepo): ViewModel() {
    fun getNewsHealth() = donateRepo.getNewsHealth()

}