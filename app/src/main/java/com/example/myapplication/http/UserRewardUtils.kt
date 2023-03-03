package com.example.myapplication.http

import com.example.myapplication.http.api.UserQuestionApi
import com.example.myapplication.http.api.UserRewardApi
import com.example.myapplication.utils.RetrofitUtils
import okhttp3.RequestBody.Companion.toRequestBody

object UserRewardUtils {


    @Throws(Exception::class)
    fun getReward(uid: String, region: String): Int {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(UserRewardApi::class.java).getReward(uid.toRequestBody(), region.toRequestBody())
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }
        return 0
    }

    @Throws(Exception::class)
    fun setReward(uid: String, region: String, reward: String): Boolean {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(UserRewardApi::class.java).setReward(uid.toRequestBody(), region.toRequestBody(), reward.toRequestBody())
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }
        return false
    }
}