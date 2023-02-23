package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityRegisterManagerBinding
import com.example.myapplication.utils.http.LoginUtils
import com.example.myapplication.utils.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterManagerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.button10.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                val flag = LoginUtils.applyManager(Prefs.userAccount, Prefs.userPassword, "1")

                withContext(Dispatchers.Main) {
                    if (flag) {
                        Toast.makeText(this@RegisterManagerActivity, "已提交申请，请等待后台确认...",Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this@RegisterManagerActivity, "失败",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
}