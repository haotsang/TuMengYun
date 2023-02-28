package com.example.myapplication.http

import com.example.myapplication.entity.AdminBean
import com.example.myapplication.entity.QuestionBean
import com.example.myapplication.http.api.AdminApi
import com.example.myapplication.http.api.QuestionApi
import com.example.myapplication.utils.RetrofitUtils
import okhttp3.RequestBody.Companion.toRequestBody

object QuestionUtils {

    @Throws(Exception::class)
    fun getQuestionsById(id: String): List<QuestionBean>? {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(QuestionApi::class.java).getQuestionsById(id)
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }
        return null
    }

    @Throws(Exception::class)
    fun insertQuestion(jsonString: String): Boolean {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(QuestionApi::class.java).insert(jsonString.toRequestBody())
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }
        return false
    }


}