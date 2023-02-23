package com.example.myapplication.utils.http

import com.example.myapplication.bean.LabelImgBean
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

object LabelImgUtils {

    const val BASE_URL = "http://192.168.1.17:8080/label_img"


    @Throws(Exception::class)
    fun getLabelImgList(labelId: String): List<LabelImgBean> {
        val list = mutableListOf<LabelImgBean>()

        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("$BASE_URL/list/${labelId}")
            .get()
            .build()
        val response: Response = client.newCall(request).execute()
        val responseData = response.body?.string()

        responseData ?: return list

        val jsonArray = JSONArray(responseData)
        for (len in 0 until jsonArray.length()) {
            val obj: JSONObject = jsonArray.getJSONObject(len)

            val userBean = LabelImgBean().apply {
                this.id = obj.optInt("id")
                this.uri = obj.optString("uri")
                this.tid = obj.optInt("tid")
            }

            list.add(userBean)
        }

        return list
    }
}