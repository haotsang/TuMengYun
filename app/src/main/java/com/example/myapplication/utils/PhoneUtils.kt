package com.example.myapplication.utils

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager

object PhoneUtils {

    private val IPPFXS4 = arrayOf(
        "1790", "1791", "1793", "1795",
        "1796", "1797", "1799"
    )
    private val IPPFXS5 = arrayOf(
        "12583", "12593", "12589",
        "12520", "10193", "11808"
    )
    private val IPPFXS6 = arrayOf("118321")



    /**
     * 获取手机服务商信息
     */
    /**
     * 获取Sim卡运营商名称
     *
     * 中国移动、如中国联通、中国电信
     *
     * @return sim卡运营商名称
     */
    fun getSimOperatorName(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        return tm?.simOperatorName
    }

    /**
     * 获取Sim卡运营商名称
     *
     * 中国移动、如中国联通、中国电信
     *
     * @return 移动网络运营商名称
     */
    fun getSimOperatorByMnc(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        val operator = (tm?.simOperator) ?: return null
        return when (operator) {
            "46000", "46002", "46007" -> "中国移动"
            "46001" -> "中国联通"
            "46003" -> "中国电信"
            else -> operator
        }
    }

    /**
     * 获取电话号码
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getNativePhoneNumber(context: Context): String? {
        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            var phone = telephonyManager.line1Number ?: return null
            if (phone.contains("+")) {
//                phone = phone.substring(3, phone.length)
            }

            phone = phone.replace(" ", "")
             return phone
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    /**
     * 判断设备是否是手机
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isPhone(context: Context): Boolean {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        return tm != null && tm.phoneType != TelephonyManager.PHONE_TYPE_NONE
    }

    /**
     * 判断sim卡是否准备好
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isSimCardReady(context: Context): Boolean {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        return tm != null && tm.simState == TelephonyManager.SIM_STATE_READY
    }




    /////////////////////////////////////////////////////////////////////////////////


    /**
     * 消除电话号码中 可能含有的 IP号码、+86、0086等前缀
     *
     * @param telNum
     * @return
     */
    fun trimTelNum(telNum: String?): String? {
        var telNum = telNum
        if (telNum == null || "" == telNum) {
            println("trimTelNum is null")
            return null
        }
        val ippfx6 = substring(telNum, 0, 6)
        val ippfx5 = substring(telNum, 0, 5)
        val ippfx4 = substring(telNum, 0, 4)
        if (telNum.length > 7 && (substring(telNum, 5, 1) == "0" || substring(
                telNum,
                5,
                1
            ) == "1" || substring(
                telNum, 5, 3
            ) == "400" || substring(
                telNum, 5, 3
            ) == "+86")
            && (inArray(ippfx5, IPPFXS5) || inArray(ippfx4, IPPFXS4))
        ) telNum = substring(telNum, 5) else if (telNum.length > 8 && (substring(
                telNum,
                6,
                1
            ) == "0" || substring(telNum, 6, 1) == "1" || substring(
                telNum, 6, 3
            ) == "400" || substring(
                telNum, 6, 3
            ) == "+86")
            && inArray(ippfx6, IPPFXS6)
        ) telNum = substring(telNum, 6)
        // remove ip dial
        telNum = telNum.replace("-", "")
        telNum = telNum.replace(" ", "")
        if (substring(telNum, 0, 4) == "0086") telNum = substring(telNum, 4) else if (substring(
                telNum,
                0,
                3
            ) == "+86"
        ) telNum = substring(telNum, 3) else if (substring(telNum, 0, 5) == "00186") telNum =
            substring(telNum, 5)
        return telNum
    }

    /**
     * 截取字符串
     * @param s
     * @param from
     * @return
     */
    private fun substring(s: String, from: Int): String {
        try {
            return s.substring(from)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun substring(s: String, from: Int, len: Int): String {
        try {
            return s.substring(from, from + len)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 判断一个字符串是否在一个字符串数组中
     * @param target
     * @param arr
     * @return
     */
    private fun inArray(target: String?, arr: Array<String>?): Boolean {
        if (arr == null || arr.isEmpty()) {
            return false
        }
        if (target == null) {
            return false
        }
        for (s in arr) {
            if (target == s) {
                return true
            }
        }
        return false
    }


}