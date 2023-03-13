package com.example.myapplication.modules.space.utils

import android.app.Activity
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

object KotlinUtils {


    fun getCourseData(url: String): InputStream? {

        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()

        val inputStream: InputStream? = response.body?.byteStream()

        return inputStream

    }
}