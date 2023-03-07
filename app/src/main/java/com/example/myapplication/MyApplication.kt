package com.example.myapplication

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.example.myapplication.activity.CrashActivity
import com.example.myapplication.entity.RegionBean
import com.example.myapplication.utils.NeverCrash
import com.example.myapplication.utils.ViewUtils
import com.google.gson.Gson
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

        val pinCode = "AAA"

    }



    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        ViewUtils.setFullScreenWindowLayoutInDisplayCutout(activity, true)
        ViewUtils.setSystemBarTransparent(activity)
        ViewUtils.initSystemBarColor(activity)
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