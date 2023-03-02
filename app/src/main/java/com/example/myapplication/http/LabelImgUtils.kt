package com.example.myapplication.http

import androidx.lifecycle.LiveData
import com.example.myapplication.entity.ApiResponse
import com.example.myapplication.entity.LabelImgBean
import com.example.myapplication.entity.ResponseBase
import com.example.myapplication.http.api.LabelImgApi
import com.example.myapplication.utils.RetrofitUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object LabelImgUtils {

    @Throws(Exception::class)
    fun getLabelImgList(labelId: String): List<LabelImgBean> {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(LabelImgApi::class.java).getLabelImgList(labelId)
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }

        return emptyList()
    }


    fun uploadImage(lid: String): ResponseBase? {
//        val part = MultipartBody.Part.createFormData("file", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
//        val fileBody: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())


        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(LabelImgApi::class.java).uploadLabelImg2(lid.toRequestBody())
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }

        return null
    }

    fun deleteLabelImg(filename: String, id: String): Boolean {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(LabelImgApi::class.java).deleteLabelImg(filename.toRequestBody(), id.toRequestBody())
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }

        return false
    }


}