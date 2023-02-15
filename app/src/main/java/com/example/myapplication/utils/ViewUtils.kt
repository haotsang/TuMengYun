package com.example.myapplication.utils

import android.content.Context
import android.content.res.Resources

object ViewUtils {

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