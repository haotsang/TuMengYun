package com.example.myapplication.http.api

import retrofit2.Call
import retrofit2.http.GET

interface VersionApi {

    @GET("version")
    fun getNewVersion(): Call<String>

}