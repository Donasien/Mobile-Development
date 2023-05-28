package com.codenesia.donasein.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codenesia.donasein.Injection.Injection
import com.codenesia.donasein.ui.main.ui.donate.DonateViewModel
import com.codenesia.donasein.ui.main.ui.home.HomeViewModel

class ViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DonateViewModel::class.java)) {
            return DonateViewModel(Injection.newsHealthInjection(context)) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel Class")
    }
}