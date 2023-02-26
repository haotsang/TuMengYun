package com.example.myapplication.http.api

import com.example.myapplication.entity.QuestionBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface QuestionApi {

    @GET("question/list/{lid}")
    fun getQuestionsById(@Path("lid") id: String): Call<List<QuestionBean>>

}