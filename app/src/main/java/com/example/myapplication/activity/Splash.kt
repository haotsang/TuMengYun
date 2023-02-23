package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.myapplication.R
import com.example.myapplication.utils.ViewUtils


class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        ViewUtils.setFullscreenCompat(this, true)

        Handler(Looper.getMainLooper()).postDelayed({
            val mainIntent = Intent(
                this,
                MainActivity::class.java
            )
            startActivity(mainIntent)

            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, 2000)
    }
}