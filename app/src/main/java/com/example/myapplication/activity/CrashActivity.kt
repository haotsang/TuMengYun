package com.example.myapplication.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.utils.ViewUtils

class CrashActivity : AppCompatActivity() {

    companion object {

        private const val INTENT_KEY_IN_THROWABLE: String = "throwable"


        fun start(context: Context, throwable: Throwable?) {
            if (throwable == null) {
                return
            }
            val intent = Intent(context, CrashActivity::class.java)
            intent.putExtra(INTENT_KEY_IN_THROWABLE, throwable)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setFullscreenCompat(this, true)
        setContentView(R.layout.activity_crash)
        val tv = findViewById<TextView>(R.id.message)

        val throwable: Throwable = (intent.getSerializableExtra(INTENT_KEY_IN_THROWABLE) as? Throwable) ?: return
        tv.text = throwable.stackTraceToString()
    }
}