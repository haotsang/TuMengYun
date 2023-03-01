package com.example.myapplication.http

import com.example.myapplication.entity.ResponseBase
import com.example.myapplication.entity.UserBean
import com.example.myapplication.utils.RetrofitUtils
import com.example.myapplication.http.api.UserApi
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object UserUtils {

    @Throws(Exception::class)
    fun getAllUsers(): List<UserBean> {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(UserApi::class.java).list()
        val response = call.execute()
        val result = response.body()
        if (result != null) {
            return result
        }

        return emptyList()
    }

    @Throws(Exception::class)
    fun register(account: String, password: String, phone: String): ResponseBase? {
        val json = JSONObject().apply {
            put("account", account)
            put("password", password)
            put("phone", phone)
        }.toString()

        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(UserApi::class.java).register(
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
        val call = retrofit.create(UserApi::class.java)
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
        val call = retrofit.create(UserApi::class.java)
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
        val call = retrofit.create(UserApi::class.java)
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
        val call = retrofit.create(UserApi::class.java)
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
        val call = retrofit.create(UserApi::class.java)
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
        val call = retrofit.create(UserApi::class.java)
            .modifyRole(account.toRequestBody(), role.toRequestBody())
        val response = call.execute()
        val result = response.body()
        if (result != null) {
            return result
        }

        return false
    }

    @Throws(Exception::class)
    fun modifyBelong(account: String, id: String): ResponseBase? {
        val retrofit = RetrofitUtils.getInstance().retrofit
        val call = retrofit.create(UserApi::class.java)
            .modifyBelong(account.toRequestBody(), id.toRequestBody())
        val response = call.execute()
        val result = response.body()
        if (result != null) {
            return result
        }

        return null
    }

}