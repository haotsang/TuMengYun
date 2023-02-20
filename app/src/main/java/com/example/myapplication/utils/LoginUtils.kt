package com.example.myapplication.utils

import com.example.myapplication.bean.UserBean
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object LoginUtils {

    const val BASE_URL = "http://192.168.1.17:8080/user"

    @Throws(Exception::class)
    fun getAllUsers(): List<UserBean> {
        val list = mutableListOf<UserBean>()

        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url(BASE_URL + "/list")
            .get()
            .build()
        val response: Response = client.newCall(request).execute()
        val responseData = response.body?.string()

        responseData ?: return list

//        val users: List<User> = JSONArray.parseArray(responseData, User::class.java)

        val jsonArray = JSONArray(responseData)
        for (len in 0 until jsonArray.length()) {
            val obj: JSONObject = jsonArray.getJSONObject(len)

            val userBean = UserBean().apply {
                this.nickname = obj.optString("nickname")
                this.account = obj.optString("account")
                this.password = obj.optString("password")
                this.phone = obj.optString("phone")
                this.role = obj.optInt("role")
                this.isAdmin = obj.optInt("isAdmin")
                this.belong = obj.optString("belong")
            }

            list.add(userBean)
        }

        return list
    }

    @Throws(Exception::class)
    fun register(username: String, password: String, phone: String): JSONObject? {
        val json = JSONObject()
        json.put("account", username)
        json.put("password", password)
        json.put("phone", phone)

//        val JSON: MediaType = "application/json; charset=utf-8".toMediaTypeOrNull() ?: MultipartBody.FORM
        val body: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("jsonString", json.toString())
            .build()
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(BASE_URL + "/register")
            .post(body)
            .build()
        try {
            val response: Response = client.newCall(request).execute()
            val responseData = response.body?.string()
            responseData ?: return null

            return JSONObject(responseData)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(Exception::class)
    fun login(username: String, password: String): JSONObject? {
        val client = OkHttpClient()
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("account", username)
            .addFormDataPart("password", password)
            .build()
        val request: Request = Request.Builder()
            .url(BASE_URL + "/login")
            .post(requestBody)
            .build()
        try {
            val response: Response = client.newCall(request).execute()
            val responseData = response.body?.string()
            responseData ?: return null

            return JSONObject(responseData)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }


    @Throws(Exception::class)
    fun getUser(username: String, password: String): UserBean? {
        val client = OkHttpClient()
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("account", username)
            .addFormDataPart("password", password)
            .build()
        val request: Request = Request.Builder()
            .url(BASE_URL + "/getUser")
            .post(requestBody)
            .build()
        try {
            val response: Response = client.newCall(request).execute()
            val responseData = response.body?.string()
            responseData ?: return null

            val json = JSONObject(responseData)
            val userBean = UserBean().apply {
                this.nickname = json.optString("nickname")
                this.account = json.optString("account")
                this.password = json.optString("password")
                this.phone = json.optString("phone")
                this.role = json.optInt("role")
                this.isAdmin = json.optInt("isAdmin")
                this.belong = json.optString("belong")
            }
            return userBean
//            val bean = JSON.parse(responseData) as UserBean
//            return bean
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(Exception::class)
    fun logout(username: String, password: String): Boolean {
        val client = OkHttpClient()
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("account", username)
            .addFormDataPart("password", password)
            .build()
        val request: Request = Request.Builder()
            .url(BASE_URL + "/logout")
            .post(requestBody)
            .build()
        try {
            val response: Response = client.newCall(request).execute()
            val responseData = response.body?.string()
            responseData ?: return false

            return responseData.toBoolean()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }



    @Throws(Exception::class)
    fun applyManager(username: String, password: String, admin: String): Boolean {
        val client = OkHttpClient()
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("account", username)
            .addFormDataPart("password", password)
            .addFormDataPart("admin", admin)
            .build()
        val request: Request = Request.Builder()
            .url(BASE_URL + "/admin")
            .post(requestBody)
            .build()
        try {
            val response: Response = client.newCall(request).execute()
            val responseData = response.body?.string()
            responseData ?: return false

            return responseData.toBoolean()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }

    @Throws(Exception::class)
    fun modifyRole(username: String, role: String): Boolean {
        val client = OkHttpClient()
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("account", username)
            .addFormDataPart("role", role)
            .build()
        val request: Request = Request.Builder()
            .url(BASE_URL + "/modifyRole")
            .post(requestBody)
            .build()
        try {
            val response: Response = client.newCall(request).execute()
            val responseData = response.body?.string()
            responseData ?: return false

            return responseData.toBoolean()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }


}