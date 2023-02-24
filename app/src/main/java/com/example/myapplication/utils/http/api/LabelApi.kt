package com.example.myapplication.utils.http.api

import com.example.myapplication.bean.LabelBean
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

interface LabelApi {

    @GET("label/list")
    fun getAll(): Call<List<LabelBean>>

    @GET("label/get/{id}")
    fun getParamUser(@Query("id") id: Int): Call<List<LabelBean>>

}