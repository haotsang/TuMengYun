package com.example.myapplication.http

import com.example.myapplication.entity.LabelQuestionBean
import com.example.myapplication.http.api.LabelQuestionApi
import com.example.myapplication.utils.RetrofitUtils
import okhttp3.RequestBody.Companion.toRequestBody

object LabelQuestionUtils {

    @Throws(Exception::class)
    fun getQuestionsById(id: String): List<LabelQuestionBean>? {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(LabelQuestionApi::class.java).getQuestionsById(id)
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
        val call = retrofit.create(LabelQuestionApi::class.java).insert(jsonString.toRequestBody())
        val response = call.execute()
        val result = response.body()

        if (result != null) {
            return result
        }
        return false
    }


}