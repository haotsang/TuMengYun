package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.bean.ResponseBase
import com.example.myapplication.bean.UserBean
import com.example.myapplication.databinding.ActivityRegisterBinding
import com.example.myapplication.utils.http.LoginUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.Utils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener {
            val username = binding.editTextTextPersonName.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            val phone = binding.editTextTextPhone.text.toString()


            if (username.length !in 6..18) {
                Toast.makeText(this, "用户名应大于6位且小于18位", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length !in 6..18) {
                Toast.makeText(this, "密码应大于6位且小于18位", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Utils.isMobileNO(phone)) {
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            register(username, password, phone)
        }

    }


    private fun register(username: String, password: String, phone: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val responseBase: ResponseBase? = try {
                LoginUtils.register(username, password, phone)
            } catch (e: Exception) {
                null
            }

            withContext(Dispatchers.Main) {
                if (responseBase != null) {
                    when (responseBase.code) {
                        200 -> {
                            Prefs.isSaveStatus = true
                            Prefs.userInfo = responseBase.data!!.toString()
                            Prefs.isLoginFromPhone = false

                            Toast.makeText(this@RegisterActivity, "注册成功，已登录！", Toast.LENGTH_LONG).show()
                            finish()
                        }
                        else -> {
                            Prefs.isSaveStatus = false
                            Prefs.userInfo = ""
                            Toast.makeText(this@RegisterActivity, responseBase.message, Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "register responseBase is null", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}