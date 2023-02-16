package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityRegisterBinding
import com.example.myapplication.utils.LoginUtils
import com.example.myapplication.utils.Utils
import org.json.JSONException
import org.json.JSONObject


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val isRegister = intent.getBooleanExtra("register", true)

        binding.toolbar.title = if (isRegister) "注册" else "登录"
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.editTextTextPhone.visibility = if (isRegister) View.VISIBLE else View.GONE
        binding.buttonLogin.visibility = if (isRegister) View.GONE else View.VISIBLE

        binding.buttonRegister.visibility = if (isRegister) View.VISIBLE else View.GONE
        binding.button8.visibility = if (isRegister) View.VISIBLE else View.GONE
        binding.button10.visibility = if (isRegister) View.VISIBLE else View.GONE


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


        binding.buttonLogin.setOnClickListener {
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

            login(username, password)
        }

    }

    private fun login(username: String, password: String) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("username", username)
            jsonObject.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Thread {
            val status = LoginUtils.login2(username, password)

            runOnUiThread {
                if (status) {
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "登录失败，此账户不存在或密码错误", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()


    }

    private fun register(username: String, password: String, phone: String) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("username", username)
            jsonObject.put("password", password)
            jsonObject.put("phone", phone)
        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Thread {
            val status = LoginUtils.register(username, password)

            runOnUiThread {
                when (status) {
                    "exists" -> {
                        Toast.makeText(this, "已存在此账户", Toast.LENGTH_SHORT).show()
                    }
                    "failed" -> {
                        Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show()
                    }
                    "ok" -> {
                        Toast.makeText(this, "注册成功，已登录", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }.start()
    }


}