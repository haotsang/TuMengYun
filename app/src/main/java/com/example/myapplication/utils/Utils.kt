package com.example.myapplication.utils

import com.example.myapplication.http.api.UserService
import okhttp3.FormBody
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    fun formatTime(date: Date?): String? {
        if (date == null) return null
        val format = SimpleDateFormat("yyyy年MM月dd日 HH时mm分", Locale.getDefault())
        return format.format(date)
    }


    fun isPhone(str: String): Boolean {
        val regex = "^(1)\\d{10}$" //正则表达式
        return str.matches(regex.toRegex())
    }

    //判断手机号格式
    fun isMobileNO(mobiles: String?): Boolean {
        if (mobiles.isNullOrEmpty()) return false
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        val telRegex = "[1][3456789]\\d{9}"
        return mobiles.matches(telRegex.toRegex())
    }


    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list() ?: return false
            for (aChildren in children) {
                val success: Boolean = deleteDir(File(dir, aChildren))
                if (!success) {
                    return false
                }
            }
        }
        return dir != null && dir.delete()
    }


    fun w() {
        val obj = JSONObject()
        obj.put("zhanQu", "")
        obj.put("zhanPin", "")
        obj.put("issue", "")
        obj.put("staff", "")
        /*
            0  上传
            1  撤销
            2  修好
        */
        obj.put("status", 1)


        val params = FormBody.Builder()
        params.add("token", "qianduan")

        val service = RetrofitUtils.getInstance().retrofit.create(UserService::class.java)
        val call = service.carReportList(params.build())

        val response = call.execute()
        val i = response.body()
        i
//        val call: Call<CangKuItem> = service.loginGet("qianduan")
//        val response: Response<CangKuItem> = call.execute()
//        val i = response.body()
        

        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@->"+ i.toString())
    }
}