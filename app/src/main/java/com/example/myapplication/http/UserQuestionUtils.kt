package com.example.myapplication.http

import com.example.myapplication.entity.ResponseBase
import com.example.myapplication.entity.UserQuestionBean
import com.example.myapplication.http.api.UserQuestionApi
import com.example.myapplication.utils.RetrofitUtils
import okhttp3.RequestBody.Companion.toRequestBody

object UserQuestionUtils {

    @Throws(Exception::class)
    fun isAlreadyAnswered(jsonString: String): ResponseBase? {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(UserQuestionApi::class.java).isAlreadyAnswered(jsonString.toRequestBody())
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }
        return null
    }

    @Throws(Exception::class)
    fun setAnswered(uid: String, qid: String, selected: String): Boolean {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(UserQuestionApi::class.java).setAnswered(uid.toRequestBody(), qid.toRequestBody(), selected.toRequestBody())
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }
        return false
    }
}