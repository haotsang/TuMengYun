package com.example.myapplication.http.api

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserQuestionApi {

    @Multipart
    @POST("user_question/already_answered")
    fun isAlreadyAnswered(
        @Part("jsonString") jsonString: RequestBody
    ): Call<Boolean>


    @Multipart
    @POST("user_question/answered")
    fun setAnswered(
        @Part("uid") uid: RequestBody,
        @Part("qid") qid: RequestBody
    ): Call<Boolean>
}