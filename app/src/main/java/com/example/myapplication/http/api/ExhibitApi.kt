package com.example.myapplication.http.api

import com.example.myapplication.modules.area.entity.ExhibitBean
import com.example.myapplication.modules.area.entity.ExhibitDesc
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ExhibitApi {

    @GET("exhibit/list/{pin}")
    fun getExhibitsByPin(@Path("pin") pin: String): Call<List<ExhibitBean>>

    @GET("exhibit/list_simple/{pin}")
    fun getSimpleExhibitsByPin(@Path("pin") pin: String): Call<List<ExhibitBean>>

    @GET("exhibit/list_desc/{parent}")
    fun getDesc(@Path("parent") parent: String): Call<List<ExhibitDesc>>
}