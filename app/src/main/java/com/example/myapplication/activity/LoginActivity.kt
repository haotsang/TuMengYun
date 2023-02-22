package com.example.myapplication.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.MyPagerAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityGroupBinding
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.Pager1Binding
import com.example.myapplication.databinding.Pager2Binding
import com.example.myapplication.utils.LoginUtils
import com.example.myapplication.utils.PhoneUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.Utils
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


        val v1 = Pager1Binding.inflate(LayoutInflater.from(this))
        v1.tvPhoneNumber.text = PhoneUtils.getNativePhoneNumber(this) ?: "186****8877"
        v1.btnAutoLogin.setOnClickListener {
            if (Utils.isMobileNO(v1.tvPhoneNumber.text.toString())) {

            }
        }
        val v2 = Pager2Binding.inflate(LayoutInflater.from(this))
        v2.buttonLogin.setOnClickListener {
            val username = v2.editTextTextPersonName.text.toString()
            val password = v2.editTextTextPassword.text.toString()

            if (username.length !in 6..18) {
                Toast.makeText(this, "用户名应大于6位且小于18位", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length !in 6..18) {
                Toast.makeText(this, "密码应大于6位且小于18位", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            login(username, password, v2.saveStatus.isChecked)
        }

        list.add(v1.root)
        list.add(v2.root)

        binding.viewPager.adapter = MyPagerAdapter(list)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.tabLayout.getTabAt(0)?.text = "一键登录"
        binding.tabLayout.getTabAt(1)?.text = "密码登录"
    }

    private fun login(username: String, password: String, saveState: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val json = try {
                LoginUtils.login(username, password)
            } catch (e: Exception) {
                null
            }

            println("______________________________________________${json.toString()}")
            withContext(Dispatchers.Main) {
                if (json != null) {
                    val msg = json.optString("msg")
                    if (msg == "登录成功") {
                        Prefs.isLogin = true
                        Prefs.isAdmin = json.optInt("role") == 2
                        Prefs.userAccount = username
                        Prefs.userPassword = password
                        Prefs.isSaveStatus = saveState

                        Toast.makeText(this@LoginActivity, "登录成功！", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Prefs.isLogin = false
                        Prefs.isAdmin = false
                        Prefs.isSaveStatus = false
                        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "login:-1", Toast.LENGTH_LONG).show()
                }

            }
        }


    }


}