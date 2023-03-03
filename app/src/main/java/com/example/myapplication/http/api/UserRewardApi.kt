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
        @Part("uid") uid: RequestBody,
        @Part("region") region: RequestBody,
    ): Call<Int>


    @Multipart
    @POST("user_reward/set")
    fun setReward(
        @Part("uid") uid: RequestBody,
        @Part("region") region: RequestBody,
        @Part("reward") reward: RequestBody,
    ): Call<Boolean>
}