package com.example.myapplication

import android.app.Application
import kotlin.properties.Delegates

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
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