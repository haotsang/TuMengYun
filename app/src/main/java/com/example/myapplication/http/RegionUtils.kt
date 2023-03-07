package com.example.myapplication.http

import com.example.myapplication.entity.RegionBean
import com.example.myapplication.utils.RetrofitUtils
import com.example.myapplication.http.api.RegionApi

object RegionUtils {

    @Throws(Exception::class)
    fun getAll(): List<RegionBean> {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(RegionApi::class.java).getAll()
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }
        return emptyList()
    }

    @Throws(Exception::class)
    fun getRegionById(id: String): RegionBean? {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(RegionApi::class.java).getRegionById(id)
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }
        return null
    }


}