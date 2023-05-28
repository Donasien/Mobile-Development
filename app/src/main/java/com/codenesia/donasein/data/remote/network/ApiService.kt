package com.codenesia.donasein.data.remote.network

import com.codenesia.donasein.data.remote.response.NewsHealthResponse
import com.codenesia.donasein.data.remote.response.RegisterResponse
import com.google.android.gms.common.api.internal.ApiKey
import retrofit2.http.*

interface ApiService {
    @GET("top-headlines")
    suspend fun getNewsHealth(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String,
    ) : NewsHealthResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("token") password: String
    ): RegisterResponse
}