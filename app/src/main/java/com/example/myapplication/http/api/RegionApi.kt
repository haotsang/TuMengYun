package com.example.myapplication.http.api

import com.example.myapplication.entity.RegionBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface RegionApi {

    @GET("region/list")
    fun getAll(): Call<List<RegionBean>>

    @GET("region/get/{id}")
    fun getRegionById(@Path("id") id: String): Call<RegionBean>


}