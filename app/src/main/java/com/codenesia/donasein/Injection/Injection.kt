package com.codenesia.donasein.Injection

import android.content.Context
import com.codenesia.donasein.data.remote.network.ApiConfig
import com.codenesia.donasein.data.repository.NewsHealthRepo

object Injection {
    fun newsHealthInjection(context: Context): NewsHealthRepo {
        val apiService = ApiConfig().getApiServiceForNewsHealth()

        return NewsHealthRepo(apiService)
    }
}