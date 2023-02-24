package com.example.myapplication.utils.http.api

import com.example.myapplication.bean.ResponseBase
import com.example.myapplication.bean.UserBean
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface LoginApi {

    @GET("user/list")
    fun list(): Call<List<UserBean>>

    @Multipart
    @POST("user/register")
    fun register(
        @Part("jsonString") jsonString: RequestBody
    ): Call<ResponseBase>

    @Multipart
    @POST("user/login")
    fun login(
        @Part("account") account: RequestBody,
        @Part("password") password: RequestBody
    ): Call<ResponseBase>

    @Multipart
    @POST("user/phone_login")
    fun loginWithPhone(
        @Part("phone") phone: RequestBody
    ): Call<ResponseBase>

    @Multipart
    @POST("user/getUser")
    fun getUser(
        @Part("account") account: RequestBody,
        @Part("password") password: RequestBody
    ): Call<UserBean>

    @Multipart
    @POST("user/logout")
    fun logout(
        @Part("account") account: RequestBody,
        @Part("password") password: RequestBody
    ): Call<Boolean>

    @Multipart
    @POST("user/admin")
    fun applyAdmin(
        @Part("account") account: RequestBody,
        @Part("password") password: RequestBody,
        @Part("admin") admin: RequestBody
    ): Call<Boolean>

    @Multipart
    @POST("user/modify_role")
    fun modifyRole(
        @Part("account") account: RequestBody,
        @Part("role") role: RequestBody
    ): Call<Boolean>

}