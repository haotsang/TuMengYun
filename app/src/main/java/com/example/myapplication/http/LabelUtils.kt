package com.example.myapplication.http

import com.example.myapplication.entity.LabelBean
import com.example.myapplication.utils.RetrofitUtils
import com.example.myapplication.http.api.LabelApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody

object LabelUtils {


   /*
        @params visible 0 全部显示  1 只显示提交的
    */
    @Throws(Exception::class)
    fun getLabelByPin(pin: String): LabelBean? {
       val retrofit = RetrofitUtils.getInstance().retrofit
       val call = retrofit.create(LabelApi::class.java).getLabelByPin(pin)
       val response = call.execute()
       val result = response.body()

       if (result != null) {
           return result
       }

        return null
    }

    @Throws(Exception::class)
    fun getOrInsert(pin: String): LabelBean? {
       val retrofit = RetrofitUtils.getInstance().retrofit
       val call = retrofit.create(LabelApi::class.java).getOrInsert(pin.toRequestBody())
       val response = call.execute()
       val result = response.body()

       if (result != null) {
           return result
       }

        return null
    }


    @Throws(Exception::class)
    fun modifyOrInsert(labelBean: LabelBean): Boolean {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        val gsonString = gson.toJson(labelBean)

        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(LabelApi::class.java).modifyOrInsert(gsonString.toRequestBody())
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result > 0
        }

        return false
    }

}