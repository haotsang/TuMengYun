package com.example.myapplication.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.myapplication.R
import com.example.myapplication.utils.extensions.toColor

object ViewUtils {

    fun setFullscreenCompat(activity: Activity, fullscreen: Boolean) {
        val window = activity.window
        val decorView = window.decorView
        if (fullscreen) {
            WindowInsetsControllerCompat(window, decorView).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            WindowInsetsControllerCompat(window, decorView).show(WindowInsetsCompat.Type.systemBars())
        }
    }

    fun setFullScreenWindowLayoutInDisplayCutout(activity: Activity, isCutout: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 设置窗口占用刘海区
            val lp = activity.window.attributes
            lp.layoutInDisplayCutoutMode =
                if (isCutout) WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES else
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
            activity.window.attributes = lp
        }
    }

    fun setSystemBarTransparent(activity: Activity) {
        val window = activity.window
        val view = window.decorView
        val flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        view.systemUiVisibility = view.systemUiVisibility or flag
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }
    }


    fun initSystemBarColor(activity: Activity?) {
        val window = activity?.window ?: return

        val statusBarColor: Int
        val navigationBarColor: Int
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                window.isStatusBarContrastEnforced = false
                window.isNavigationBarContrastEnforced = false

                statusBarColor = Color.TRANSPARENT
                navigationBarColor = Color.TRANSPARENT
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                window.navigationBarDividerColor = Color.TRANSPARENT

                statusBarColor = Color.TRANSPARENT
                navigationBarColor = Color.TRANSPARENT
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                statusBarColor = Color.TRANSPARENT
                navigationBarColor = activity.toColor(R.color.ripper)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                statusBarColor = Color.TRANSPARENT
                navigationBarColor = activity.toColor(R.color.ripper)
            }
            else -> {
                statusBarColor = activity.toColor(R.color.ripper)
                navigationBarColor = activity.toColor(R.color.ripper)
            }
        }

        window.statusBarColor = statusBarColor
        window.navigationBarColor = navigationBarColor
    }

    fun setBarsFontLightColor(activity: Activity, status: Boolean, navigation: Boolean) {
        val decorView = activity.window.decorView
        WindowInsetsControllerCompat(activity.window, decorView).isAppearanceLightStatusBars = status
        WindowInsetsControllerCompat(activity.window, decorView).isAppearanceLightNavigationBars = navigation
    }

    fun dp2px(dp: Float): Int {
        val metrics = Resources.getSystem().displayMetrics
        return (dp * metrics.density + 0.5f).toInt()
    }


    fun getWindowHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun getWindowWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

}