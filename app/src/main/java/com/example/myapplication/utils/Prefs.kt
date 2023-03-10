package com.example.myapplication.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.myapplication.MyApplication

object Prefs {

    private val context = MyApplication.instance()

    //双重校验锁实现单例
    val manager: SharedPreferences by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getSharedPreferences(name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun clear() {
        manager.edit().clear().apply()
    }



    var isSaveStatus: Boolean
        get() = manager.getBoolean("sp_isSaveStatus", false)
        set(value) = put("sp_isSaveStatus", value)
    var isLoginFromPhone: Boolean
        get() = manager.getBoolean("sp_isLoginFromPhone", false)
        set(value) = put("sp_isLoginFromPhone", value)

    var userInfo: String
        get() = manager.getString("sp_user_info", "")!!
        set(value) = put("sp_user_info", value)



    var labelSettingItemStatus: Boolean
        get() = manager.getBoolean("sp_labelSettingItemStatus", false)
        set(value) = put("sp_labelSettingItemStatus", value)
    var tuiSong: Boolean
        get() = manager.getBoolean("sp_tuisong", false)
        set(value) = put("sp_tuisong", value)


    /**
     * 保存单个数据
     *
     * @param key    键值
     * @param object 存储的内容
     */
    private fun put(key: String, `object`: Any) {
        when (`object`) {
            is String -> manager.edit().putString(key, `object`).apply()
            is Int -> manager.edit().putInt(key, `object`).apply()
            is Boolean -> manager.edit().putBoolean(key, `object`).apply()
            is Float -> manager.edit().putFloat(key, `object`).apply()
            is Long -> manager.edit().putLong(key, `object`).apply()
            else -> manager.edit().putString(key, `object`.toString()).apply()
        }
    }

    /**
     * 同时保存多条数据
     *
     * @param map 存储的数据
     */
    fun add(map: Map<String, Any>) {
        val set = map.keys
        for (key in set) {
            when (val `object` = map[key]) {
                is String -> manager.edit().putString(key, `object` as String?).apply()
                is Int -> manager.edit().putInt(key, (`object` as Int?)!!).apply()
                is Boolean -> manager.edit().putBoolean(key, (`object` as Boolean?)!!).apply()
                is Float -> manager.edit().putFloat(key, (`object` as Float?)!!).apply()
                is Long -> manager.edit().putLong(key, (`object` as Long?)!!).apply()
                else -> manager.edit().putString(key, `object`!!.toString()).apply()
            }
        }
    }
}