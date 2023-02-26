package com.example.myapplication.http.api

import com.example.myapplication.entity.AdminBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface AdminApi {

    @GET("admin/list")
    fun getAll(): Call<List<AdminBean>>

    @GET("admin/get/{id}")
    fun getAdminById(@Path("id") id: String): Call<AdminBean>



}