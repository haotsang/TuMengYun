package com.example.myapplication.utils.http

import com.example.myapplication.bean.LabelBean
import okhttp3.*
import org.json.JSONObject

object LabelUtils {

    const val BASE_URL = "http://192.168.1.17:8080/label"

   /*
        @params visible 0 全部显示  1 只显示提交的
    */
    @Throws(Exception::class)
    fun getLabel(labelId: String): LabelBean? {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("$BASE_URL/get/${labelId}")
            .get()
            .build()
        val response: Response = client.newCall(request).execute()
        val responseData = response.body?.string()

        responseData ?: return null

        val obj = JSONObject(responseData)
        val bean = LabelBean().apply {
            this.id = obj.optInt("id")
            this.title = obj.optString("title")
            this.content = obj.optString("content")
            this.question = obj.optInt("question")
            this.visible = obj.optInt("visible")
            this.region = obj.optInt("region")
            this.type = obj.optInt("type")
        }

       println(obj.toString())
        return bean
    }


    @Throws(Exception::class)
    fun modify(labelBean: LabelBean): Boolean {
        val json = JSONObject()
        json.put("id", labelBean.id)
        json.put("title", labelBean.title)
        json.put("content", labelBean.content)
        json.put("question", labelBean.question)
        json.put("visible", labelBean.visible)
        json.put("region", labelBean.region)
        json.put("type", labelBean.type)

        val client = OkHttpClient()
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("jsonString", json.toString())
            .build()
        val request: Request = Request.Builder()
            .url(BASE_URL + "/modify")
            .post(requestBody)
            .build()
        val response: Response = client.newCall(request).execute()
        val responseData = response.body?.string()
        responseData ?: return false

        return responseData.toInt() > 0
    }

}