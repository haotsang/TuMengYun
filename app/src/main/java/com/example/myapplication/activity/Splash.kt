package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.myapplication.R
import com.example.myapplication.utils.ViewUtils


class Splash : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setFullscreenCompat(this, true)
        setContentView(R.layout.splash)

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