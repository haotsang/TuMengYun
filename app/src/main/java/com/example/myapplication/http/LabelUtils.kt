package com.example.myapplication.http

import com.example.myapplication.entity.LabelBean
import com.example.myapplication.utils.RetrofitUtils
import com.example.myapplication.http.api.LabelApi
import com.google.gson.Gson
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody

object LabelUtils {


   /*
        @params visible 0 全部显示  1 只显示提交的
    */
    @Throws(Exception::class)
    fun getLabel(labelId: String): LabelBean? {
       val retrofit = RetrofitUtils.getInstance().retrofit
       val call = retrofit.create(LabelApi::class.java).getLabel(labelId)
       val response = call.execute()
       val result = response.body()

       if (result != null) {
           return result
       }

        return null
    }


    @Throws(Exception::class)
    fun modify(labelBean: LabelBean): Boolean {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(LabelApi::class.java).modify(Gson().toJson(labelBean).toRequestBody())
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result > 0
        }

        return false
    }

}