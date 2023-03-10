package com.example.myapplication

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.myapplication.activity.CrashActivity
import com.example.myapplication.utils.NeverCrash
import kotlin.properties.Delegates

class MyApplication : Application(), Application.ActivityLifecycleCallbacks {

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerActivityLifecycleCallbacks(this)

        NeverCrash.init { t, e ->
            CrashActivity.start(this, e)
            System.exit(1)
        }
    }

    companion object {

        //双重校验锁实现单例
        val instance2: MyApplication by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MyApplication()
        }

        var instance: MyApplication by Delegates.notNull()

        fun instance() = instance

    }



    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {

    }
    override fun onActivityResumed(activity: Activity) {

    }
    override fun onActivityPaused(activity: Activity) {

    }
    override fun onActivityStopped(activity: Activity) {

    }
    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {

    }
    override fun onActivityDestroyed(activity: Activity) {

    }

}