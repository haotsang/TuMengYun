package com.example.myapplication.http.api

import androidx.lifecycle.LiveData
import com.example.myapplication.entity.ApiResponse
import com.example.myapplication.entity.LabelImgBean
import com.example.myapplication.entity.ResponseBase
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface LabelImgApi {

    @GET("label_img/list/{lid}")
    fun getLabelImgList(@Path("lid") lid: String): Call<List<LabelImgBean>>

    @Multipart
    @POST("label_img/upload_image")
    fun uploadLabelImg(@Part part: MultipartBody.Part, @Part("lid") lid: RequestBody): Call<String>

    @Multipart
    @POST("label_img/upload_image")
    fun uploadLabelImg2(@Part("lid") lid: RequestBody): Call<ResponseBase>

    @Multipart
    @POST("label_img/delete_image")
    fun deleteLabelImg(@Part("filename") filename: RequestBody, @Part("id") id: RequestBody): Call<Boolean>
}