package com.example.myapplication.http

import com.example.myapplication.entity.CangKuItem
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException


object HttpUtils {


    fun uploadImageFile(file: File, id: String) {
        val url = "http://106.15.94.206:8084/upload/image"
        val client = OkHttpClient()

        val filebody: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("token", "qianduan")
            .addFormDataPart("file", id + "-" + file.name, filebody)
            .build()

        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("-------------------->\n${e.stackTraceToString()}")
            }
            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()
                println("-------------------->${result}")
            }
        })
    }

    fun getLingJianAll(): String? {
        try {
            val params = FormBody.Builder()
            params.add("token", "qianduan")

            //创建http客户端
            val client = OkHttpClient()
            //创建http请求
            val request = Request.Builder()
                .url("http://106.15.94.206:8084/lingjian/getLingJianAll")
                .post(params.build())
                .build();
            //执行发送的指令
            val response = client.newCall(request).execute()
            //获取后端数据
            val res = response.body?.string()

            return res
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun jsonToList(json: String):List<CangKuItem> {

        val list = mutableListOf<CangKuItem>()

        val jsonObject = JSONObject(json)
        val jsonArray = jsonObject.getJSONArray("data")
        for (len in 0 until jsonArray.length()) {
            val obj: JSONObject = jsonArray.getJSONObject(len)
            val lingjian_id = obj.optInt("lingjian_id")
            val lingjian_Daihao = obj.optString("lingjian_Daihao")
            val lingjian_Name = obj.optString("lingjian_Name")
            val lingjian_zuoyong = obj.optString("lingjian_zuoyong")
            val lingjian_shebeiid = obj.optInt("lingjian_shebeiid")
            val lingjian_zuobiao = obj.optString("lingjian_zuobiao")
            val lingjian_count = obj.optInt("lingjian_count")
            val shebei = obj.optString("shebei")

            val item = CangKuItem(
                lingjian_id,
                lingjian_Daihao,
                lingjian_Name,
                lingjian_zuoyong,
                lingjian_shebeiid,
                lingjian_zuobiao,
                lingjian_count,
                shebei
            )
            list.add(item)

        }

        return list
    }

}