package com.codenesia.donasein.ui.main.ui.donate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codenesia.donasein.data.repository.NewsHealthRepo

class DonateViewModel(private val newsHealthRepo: NewsHealthRepo) : ViewModel() {

    fun getNewsHealth() = newsHealthRepo.getNewsHealth()

}