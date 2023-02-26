package com.example.myapplication.http

import com.example.myapplication.entity.LabelImgBean
import com.example.myapplication.utils.RetrofitUtils
import com.example.myapplication.http.api.LabelImgApi

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
}