package com.example.myapplication.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat

class PhoneUtils {

    /**
     * 获取手机服务商信息
     */
    @SuppressLint("MissingPermission")
    fun getProvidersName(context: Context): String {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var ProvidersName = "N/A"
        try {
            val IMSI = telephonyManager.subscriberId
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            System.out.println(IMSI)
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = "中国移动"
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = "中国联通"
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ProvidersName
    }

    /**
     * 获取电话号码
     */
    fun getNativePhoneNumber(context: Context): String? {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var NativePhoneNumber: String? = null
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_NUMBERS
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            NativePhoneNumber = telephonyManager.line1Number
        }

        return NativePhoneNumber
    }

}