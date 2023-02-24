package com.example.myapplication.utils.http

import com.example.myapplication.bean.AdminBean
import com.example.myapplication.utils.RetrofitUtils
import com.example.myapplication.utils.http.api.AdminApi

object AdminUtils {

    @Throws(Exception::class)
    fun getAll(): List<AdminBean> {
        val list = mutableListOf<AdminBean>()

        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(AdminApi::class.java).getAll()
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }
        return list
    }

}