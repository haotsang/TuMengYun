package com.example.myapplication.http.api

import com.example.myapplication.entity.LabelImgBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface LabelImgApi {

    @GET("label_img/list/{tid}")
    fun getLabelImgList(@Path("tid") tid: String): Call<List<LabelImgBean>>

}