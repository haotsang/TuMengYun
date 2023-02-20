package com.example.myapplication.utils

import com.example.myapplication.bean.AdminBean
import com.example.myapplication.bean.UserBean
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

object AdminUtils {

    const val BASE_URL = "http://192.168.1.17:8080/admin"

    @Throws(Exception::class)
    fun getAll(): List<AdminBean> {
        val list = mutableListOf<AdminBean>()

        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url(BASE_URL + "/list")
            .get()
            .build()
        val response: Response = client.newCall(request).execute()
        val responseData = response.body?.string()

        responseData ?: return list
println("@@@@@"+responseData)

        val jsonArray = JSONArray(responseData)
        for (len in 0 until jsonArray.length()) {
            val obj: JSONObject = jsonArray.getJSONObject(len)

            val userBean = AdminBean().apply {
                this.id = obj.optLong("id")
                this.name = obj.optString("name")
                this.account = obj.optString("account")
                this.password = obj.optString("password")
                this.contactName = obj.optString("contactName")
                this.contractPhone = obj.optString("contractPhone")
                this.address = obj.optString("address")
                this.enable = obj.optInt("enable")
            }

            list.add(userBean)
        }

        return list
    }

}