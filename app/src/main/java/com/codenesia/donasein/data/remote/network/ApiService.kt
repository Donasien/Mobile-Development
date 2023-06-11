package com.codenesia.donasein.data.remote.network

import android.provider.ContactsContract.DisplayPhoto
import com.codenesia.donasein.data.remote.response.*
import com.google.android.gms.common.api.internal.ApiKey
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @GET("top-headlines")
    suspend fun getNewsHealth(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String,
    ) : NewsHealthResponse

    @GET("all-donation")
    suspend fun getAllDonation() : DonateResponse



    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("email") email: String,
        @Field("fullname") fullname: String,
        @Field("token") token: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("token") token: String
    ): LogoutResponse

    @FormUrlEncoded
    @POST("google")
    suspend fun loginUserGoogle(
        @Field("email") email: String,
        @Field("fullname") fullname: String,
        @Field("token") token: String,
        @Field("photo") photo: String,
    ): LoginGoogleResponse

    @FormUrlEncoded
    @POST("logout")
    suspend fun logoutUser(
        @Field("token") token: String
    ): LogoutResponse

    @GET("profile")
    suspend fun getProfile(
        @Query("token") token: String
    ) : ProfileResponse

    @FormUrlEncoded
    @POST("profile")
    suspend fun updateProfile(
        @Field("token") token: String,
        @Field("fullname") fullname: String,
        @Field("address") address: String,
        @Field("phone") phone: String,
        @Field("kk") kk: String,
        @Field("rekening") rekening: String,
        @Field("email") email: String,
        @Field("gender") gender: String,
        @Field("bank") bank: String
    ): ProfileResponse

    @Multipart
    @POST("submit-donation")
    suspend fun submitDonation(
        @Part("token") token: RequestBody,
        @Part("title") title: RequestBody,
        @Part("target_amount") target_amount: RequestBody,
        @Part("end_date") end_date: RequestBody,
        @Part("description") desciption: RequestBody,
        @Part cover_photo: MultipartBody.Part,
        @Part ktp_photo: MultipartBody.Part,
        @Part medical_photo: MultipartBody.Part,
        @Part disease_photo: MultipartBody.Part,
        @Part sktm_photo: MultipartBody.Part,
        @Part("result_alzheimer") result_alzheimer: RequestBody,
        @Part("result_lung") result_lung: RequestBody,
        @Part("type_disaster") type_disaster: RequestBody
    ) : SubmitDonationResponse

    @GET("donation-status")
    suspend fun getStatusDonasi(
        @Query("token") token: String
    ) : DonationStatusResponse

    @Multipart
    @POST("predict-disease")
    suspend fun getPredictResult(
        @Part("token") token: RequestBody,
        @Part file: MultipartBody.Part
    ) : MedicalApiResponse



    @GET("detail-donation")
    suspend fun getDetailById(
        @Query("donation_id") donation_id: Int
    ) : DetailDonateResponse


}