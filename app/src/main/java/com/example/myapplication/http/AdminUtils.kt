package com.example.myapplication.http

import com.example.myapplication.entity.AdminBean
import com.example.myapplication.entity.LabelBean
import com.example.myapplication.utils.RetrofitUtils
import com.example.myapplication.http.api.AdminApi
import com.example.myapplication.http.api.LabelApi

object AdminUtils {

    @Throws(Exception::class)
    fun getAll(): List<AdminBean> {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(AdminApi::class.java).getAll()
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }
        return emptyList()
    }

    @Throws(Exception::class)
    fun getAdminById(id: String): AdminBean? {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(AdminApi::class.java).getAdminById(id)
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }
        return null
    }


}