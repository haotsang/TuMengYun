package com.example.myapplication.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ErrorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tv = TextView(this)
        tv.setTextColor(Color.RED)
        tv.text = intent.getStringExtra("error")

        setContentView(tv)
    }
}