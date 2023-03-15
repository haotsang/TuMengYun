package com.example.myapplication.http

import com.example.myapplication.http.api.ExhibitApi
import com.example.myapplication.modules.area.entity.ExhibitBean
import com.example.myapplication.modules.area.entity.ExhibitDesc
import com.example.myapplication.utils.RetrofitUtils

object ExhibitUtils {

    @Throws(Exception::class)
    fun getExhibitsByPin(pin: String): List<ExhibitBean> {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(ExhibitApi::class.java).getExhibitsByPin(pin)
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }

        return emptyList()
    }

    @Throws(Exception::class)
    fun getSimpleExhibitsByPin(pin: String): List<ExhibitBean> {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(ExhibitApi::class.java).getSimpleExhibitsByPin(pin)
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }

        return emptyList()
    }

    @Throws(Exception::class)
    fun getDesc(parent: String): List<ExhibitDesc> {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(ExhibitApi::class.java).getDesc(parent)
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }

        return emptyList()
    }

}
