package com.example.myapplication.http.api

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserRewardApi {

    @Multipart
    @POST("user_reward/get")
    fun getReward(
        @Part("gson") gson: RequestBody
    ): Call<Int>


    @Multipart
    @POST("user_reward/set")
    fun setReward(
        @Part("gson") gson: RequestBody,
    ): Call<Boolean>
}