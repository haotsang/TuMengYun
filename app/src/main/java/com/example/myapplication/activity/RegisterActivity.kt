package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRegisterBinding
import com.example.myapplication.utils.Utils

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val isRegister = intent.getBooleanExtra("register", true)

        binding.toolbar.title = if (isRegister) "注册" else "登录"

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


    }

    private fun register(username: String, password: String, phone: String) {

    }


}