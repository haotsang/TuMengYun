package com.example.myapplication.http

import com.example.myapplication.entity.CangKuItem
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException


object HttpUtils {


    fun uploadImageFileTest(file: File) {
        val url = "http://192.168.1.10:8085/label_img/upload_image"
        val client = OkHttpClient()

        val part = MultipartBody.Part.createFormData("file", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))

        val body: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addPart(part)
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

            //??????http?????????
            val client = OkHttpClient()
            //??????http??????
            val request = Request.Builder()
                .url("http://106.15.94.206:8084/lingjian/getLingJianAll")
                .post(params.build())
                .build();
            //?????????????????????
            val response = client.newCall(request).execute()
            //??????????????????
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