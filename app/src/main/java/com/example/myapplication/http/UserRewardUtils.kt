package com.example.myapplication.http

import com.example.myapplication.http.api.UserRewardApi
import com.example.myapplication.utils.RetrofitUtils
import okhttp3.RequestBody.Companion.toRequestBody

object UserRewardUtils {

    @Throws(Exception::class)
    fun setReward(gson: String): Boolean {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(UserRewardApi::class.java).setReward(gson.toRequestBody())
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }
        return false
    }
}