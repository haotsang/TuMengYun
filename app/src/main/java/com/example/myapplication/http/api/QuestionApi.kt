package com.example.myapplication.http.api

import com.example.myapplication.entity.QuestionBean
import com.example.myapplication.entity.ResponseBase
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface QuestionApi {

    @GET("question/list/{lid}")
    fun getQuestionsById(@Path("lid") id: String): Call<List<QuestionBean>>


    @Multipart
    @POST("question/insert")
    fun insert(
        @Part("jsonString") jsonString: RequestBody
    ): Call<Boolean>
}