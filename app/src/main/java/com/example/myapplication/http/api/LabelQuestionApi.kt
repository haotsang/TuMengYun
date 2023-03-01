package com.example.myapplication.http.api

import com.example.myapplication.entity.LabelQuestionBean
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface LabelQuestionApi {

    @GET("question/list/{lid}")
    fun getQuestionsById(@Path("lid") id: String): Call<List<LabelQuestionBean>>


    @Multipart
    @POST("question/insert")
    fun insert(
        @Part("jsonString") jsonString: RequestBody
    ): Call<Boolean>
}