package com.example.myapplication.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.adapter.MyPagerAdapter
import com.example.myapplication.bean.ResponseBase
import com.example.myapplication.bean.UserBean
import com.example.myapplication.databinding.*
import com.example.myapplication.utils.http.LoginUtils
import com.example.myapplication.utils.PhoneUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.Utils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)


        val permissions = mutableListOf<String>()
        permissions.add(Manifest.permission.READ_PHONE_STATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            permissions.add(Manifest.permission.READ_PHONE_NUMBERS)
        }
        ActivityCompat.requestPermissions(
            this,
            permissions.toTypedArray(),
            100
        )

        val list = mutableListOf<View>()


        val phoneNumber = PhoneUtils.trimTelNum(PhoneUtils.getNativePhoneNumber(this))
        val v1 = ViewLoginPager1Binding.inflate(LayoutInflater.from(this))
        val listener1 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val str = v1.tvPhoneNumber.text.trim().toString()
                v1.btnAutoLogin.isEnabled = Utils.isMobileNO(str)
            }
        }
        v1.tvPhoneNumber.addTextChangedListener(listener1)
        v1.tvPhoneNumber.hint = phoneNumber
        v1.tvPhoneNumber.text = phoneNumber?.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(),"$1****$2") ?: "未知号码"

        v1.btnAutoLogin.setOnClickListener {
            if (Utils.isMobileNO(phoneNumber)) {
                loginWithPhone(phoneNumber!!, saveState = true)
            } else {
                Toast.makeText(this, "未知号码，自动登录失败", Toast.LENGTH_SHORT).show()
            }
        }
        val v2 = ViewLoginPager2Binding.inflate(LayoutInflater.from(this))

        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val len1 = v2.editTextTextPersonName.text.trim().toString().length
                val len2 = v2.editTextTextPassword.text.trim().toString().length
                v2.buttonLogin.isEnabled = len1 in 6..18 && len2 in 6..18
            }
        }
        v2.editTextTextPersonName.addTextChangedListener(listener)
        v2.editTextTextPassword.addTextChangedListener(listener)
        v2.editTextTextPersonName.setText("")
        v2.editTextTextPassword.setText("")
        v2.buttonLogin.setOnClickListener {
            val username = v2.editTextTextPersonName.text.toString()
            val password = v2.editTextTextPassword.text.toString()

//            if (username.length !in 6..18) {
//                Toast.makeText(this, "用户名应大于6位且小于18位", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (password.length !in 6..18) {
//                Toast.makeText(this, "密码应大于6位且小于18位", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

            login(username, password, v2.saveStatus.isChecked)
        }
        v2.loginRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        v2.loginForgetable.setOnClickListener {  }


        list.add(v1.root)
        list.add(v2.root)

        binding.viewPager.adapter = MyPagerAdapter(list)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.tabLayout.getTabAt(0)?.text = "一键登录"
        binding.tabLayout.getTabAt(1)?.text = "密码登录"
    }

    private fun login(username: String, password: String, saveState: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val responseBase: ResponseBase? = try {
                LoginUtils.login(username, password)
            } catch (e: Exception) {
                null
            }

            withContext(Dispatchers.Main) {
                if (responseBase != null) {
                    if (responseBase.code == 200) {
                        Prefs.isSaveStatus = saveState
                        Prefs.userInfo = responseBase.data!!.toString()
                        Prefs.isLoginFromPhone = false

                        Toast.makeText(this@LoginActivity, "登录成功！", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Prefs.isSaveStatus = false
                        Prefs.userInfo = ""
                        Toast.makeText(this@LoginActivity, responseBase.message, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "login responseBase is null", Toast.LENGTH_LONG).show()
                }

            }
        }


    }
    private fun loginWithPhone(phone: String, saveState: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val responseBase: ResponseBase? = try {
                LoginUtils.loginWithPhone(phone)
            } catch (e: Exception) {
                null
            }
            withContext(Dispatchers.Main) {
                if (responseBase != null && responseBase.code == 200) {
                    Prefs.isSaveStatus = saveState
                    Prefs.userInfo = responseBase.data!!.toString()
                    Prefs.isLoginFromPhone = true

                    Toast.makeText(this@LoginActivity, "登录成功！", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Prefs.isSaveStatus = false
                    Prefs.userInfo = ""
                    Toast.makeText(this@LoginActivity, "自动登录失败，${responseBase?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }


    }


}