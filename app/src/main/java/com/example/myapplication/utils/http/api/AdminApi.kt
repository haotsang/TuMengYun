package com.example.myapplication.utils.http.api

import com.example.myapplication.bean.AdminBean
import retrofit2.Call
import retrofit2.http.GET


interface AdminApi {

    @GET("admin/list")
    fun getAll(): Call<List<AdminBean>>
}