package com.example.myapplication.http.api

import com.example.myapplication.entity.LabelBean
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface LabelApi {

    @GET("label/list")
    fun getAll(): Call<List<LabelBean>>

    @GET("label/get/{region}")
    fun getLabel(@Path("region") id: String): Call<LabelBean>


    @Multipart
    @POST("label/modify")
    fun modify(
        @Part("jsonString") jsonString: RequestBody,
    ): Call<Int>


    @POST("label/form")
    @FormUrlEncoded
    fun testFormUrlEncoded1(
        @Field("username") name: String?,
        @Field("age") age: Int,
    ): Call<ResponseBody?>?
}