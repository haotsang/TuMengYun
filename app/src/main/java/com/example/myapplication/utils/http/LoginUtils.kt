package com.example.myapplication.utils.http

import com.example.myapplication.bean.ResponseBase
import com.example.myapplication.bean.UserBean
import com.example.myapplication.utils.RetrofitUtils
import com.example.myapplication.utils.http.api.LoginApi
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object LoginUtils {

    @Throws(Exception::class)
    fun getAllUsers(): List<UserBean> {
        val list = mutableListOf<UserBean>()

        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(LoginApi::class.java).list()
        val response = call.execute()
        val result = response.body()
        if (result != null) {
            return result
        }

        return list
    }

    @Throws(Exception::class)
    fun register(account: String, password: String, phone: String): ResponseBase? {
        val json = JSONObject().apply {
            put("account", account)
            put("password", password)
            put("phone", phone)
        }.toString()

        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(LoginApi::class.java).register(
            json.toRequestBody(),
        )
        val response = call.execute()
        val result = response.body()
        if (result != null) {
            return result
        }

        return null
    }

    @Throws(Exception::class)
    fun login(account: String, password: String): ResponseBase? {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(LoginApi::class.java)
            .login(account.toRequestBody(), password.toRequestBody())
        val response = call.execute()
        val result = response.body()
        if (result != null) {
            return result
        }

        return null
    }

    @Throws(Exception::class)
    fun loginWithPhone(phone: String): ResponseBase? {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(LoginApi::class.java)
            .loginWithPhone(phone.toRequestBody())
        val response = call.execute()
        val result = response.body()
        if (result != null) {
            return result
        }

        return null
    }


    @Throws(Exception::class)
    fun getUser(account: String, password: String): UserBean? {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(LoginApi::class.java)
            .getUser(account.toRequestBody(), password.toRequestBody())
        val response = call.execute()
        val result = response.body()
        if (result != null) {
            return result
        }

        return null
    }


    @Throws(Exception::class)
    fun logout(account: String, password: String): Boolean {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(LoginApi::class.java)
            .logout(account.toRequestBody(), password.toRequestBody())
        val response = call.execute()
        val result = response.body()
        if (result != null) {
            return result
        }

        return false
    }

    @Throws(Exception::class)
    fun applyAdmin(account: String, password: String, admin: String): Boolean {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(LoginApi::class.java)
            .applyAdmin(account.toRequestBody(), password.toRequestBody(), admin.toRequestBody())
        val response = call.execute()
        val result = response.body()
        if (result != null) {
            return result
        }

        return false
    }

    @Throws(Exception::class)
    fun modifyRole(account: String, role: String): Boolean {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(LoginApi::class.java)
            .modifyRole(account.toRequestBody(), role.toRequestBody())
        val response = call.execute()
        val result = response.body()
        if (result != null) {
            return result
        }

        return false
    }

}