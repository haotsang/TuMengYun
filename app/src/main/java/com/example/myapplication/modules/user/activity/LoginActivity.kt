package com.example.myapplication.modules.user.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.activity.BaseActivity
import com.example.myapplication.modules.user.fragment.RegisterAdminFragment
import com.example.myapplication.modules.user.fragment.RegisterUserFragment
import com.example.myapplication.modules.user.fragment.UserLoginFragment

class LoginActivity : BaseActivity() {

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