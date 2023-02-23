package com.example.myapplication.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

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

        val throwable: Throwable = (intent.getSerializableExtra(INTENT_KEY_IN_THROWABLE) as? Throwable) ?: return

        setContentView(TextView(this).apply {
            text = throwable.stackTraceToString()
        })
    }
}