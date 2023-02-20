package com.example.myapplication

import android.app.Application
import com.example.myapplication.utils.Prefs
import kotlin.properties.Delegates

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        Prefs.curRegionId = "{id: 1, name: '梦暴科技馆'}"
    }

    companion object {

        //双重校验锁实现单例
        val instance2: MyApplication by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MyApplication()
        }

        var instance: MyApplication by Delegates.notNull()

        fun instance() = instance


    }

}