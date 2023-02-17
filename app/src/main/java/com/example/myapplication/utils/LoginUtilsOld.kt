package com.example.myapplication.utils

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object LoginUtilsOld {

    const val BASE_URL = "http://192.168.1.17:8083/"

    fun getAllUsers(): List<String> {
        val list = mutableListOf<String>()

        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url(BASE_URL + "getUsers")
            .build()
        val response: Response = client.newCall(request).execute()
        val responseData = response.body?.string()

        responseData ?: return list

        val jsonArray = JSONArray(responseData)
        for (len in 0 until jsonArray.length()) {
            val obj: JSONObject = jsonArray.getJSONObject(len)
            list.add(obj.toString())
        }

        return list
    }


    fun login2(username: String, password: String): Boolean {
        val url = "${BASE_URL}login/${username}/${password}"

        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url) //这个是与我合作的后台 具有我注册过的账号密码的信息并且返回一个判断数值
            .build()
        try {
            val response: Response = client.newCall(request).execute()
            val responseData = response.body?.string()

            if (responseData == "ok") {
                return true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    fun register(username: String, password: String): String? {
        val url = "${BASE_URL}addUser/${username}/${password}"

        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url) //这个是与我合作的后台 具有我注册过的账号密码的信息并且返回一个判断数值
            .build()
        try {
            val response: Response = client.newCall(request).execute()
            val responseData = response.body?.string()

            return responseData
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }



    fun login(username: String, password: String) {
        val url = "http://192.168.1.17:8083/users/register"

        val client = OkHttpClient()
        val JSON: MediaType = "application/json; charset=utf-8".toMediaTypeOrNull() ?: MultipartBody.FORM
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(JSON)
            .addFormDataPart("username", username)
            .addFormDataPart("password", password)
            .build()
        val request: Request = Request.Builder()
            .url("http://yslgdym.cn/tp5/public/index.php/login") //这个是与我合作的后台 具有我注册过的账号密码的信息并且返回一个判断数值
            .post(requestBody)
            .build()
        try {
            val response: Response = client.newCall(request).execute()
            val responseData = response.body?.string()


            System.out.println("我是responseData$responseData") //这是我用来查看返回值的代码 进行对比
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}