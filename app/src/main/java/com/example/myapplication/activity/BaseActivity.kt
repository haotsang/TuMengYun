package com.example.myapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.myapplication.utils.ViewUtils

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewUtils.setFullScreenWindowLayoutInDisplayCutout(this, true)
        ViewUtils.setSystemBarTransparent(this)
        ViewUtils.initSystemBarColor(this)
    }
}