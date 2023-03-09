package com.example.myapplication.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.fragment.RegisterAdminFragment
import com.example.myapplication.fragment.RegisterUserFragment
import com.example.myapplication.fragment.UserLoginFragment

class LoginActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context, type: String) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra("type", type)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val fragment = when(intent.getStringExtra("type")) {
            "login" -> UserLoginFragment()
            "register" -> RegisterUserFragment()
            "admin" -> RegisterAdminFragment()
            else -> null
        } ?: return


        supportFragmentManager
            .beginTransaction()
            .replace(R.id.login_content, fragment, "login")
            .commitNowAllowingStateLoss()

    }
}