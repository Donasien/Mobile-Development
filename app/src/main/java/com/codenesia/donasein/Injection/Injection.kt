package com.codenesia.donasein.Injection

import android.content.Context
import com.codenesia.donasein.data.remote.network.ApiConfig
import com.codenesia.donasein.data.repository.DonateRepo
import com.codenesia.donasein.data.repository.UserRepo

object Injection {

    fun userInjection(context: Context): UserRepo {
        val apiService = ApiConfig().getApiServiceForUser()
        return UserRepo(apiService)
    }

    fun allDonate(context: Context): DonateRepo {
        val apiService = ApiConfig().getApiServiceForUser()
        return DonateRepo(apiService)
    }

    fun apiMedicalInjection(context: Context): DonateRepo {
        val apiService = ApiConfig().getApiServiceForMedicalAPI()
        return DonateRepo(apiService)
    }

    fun newsHealthInjection(context: Context): DonateRepo {
        val apiService = ApiConfig().getApiServiceForNewsHealth()
        return DonateRepo(apiService)
    }
}