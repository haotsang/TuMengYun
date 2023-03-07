package com.example.myapplication.activity.user

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityRegisterManagerBinding
import com.example.myapplication.utils.livebus.LiveDataBus
import com.example.myapplication.view.CustomDialog
import com.example.myapplication.viewmodel.UserViewModel

class RegisterManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterManagerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val user = UserViewModel.user
        if (user == null) {
            Toast.makeText(this, "未登录，请先登录", Toast.LENGTH_SHORT).show()
            return
        }

        LiveDataBus.with("livebus_login").observe(this) {
            val pair = it as Pair<Boolean, String>
            if (pair.first) {
                Toast.makeText(this@RegisterManagerActivity, pair.second, Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this@RegisterManagerActivity, pair.second, Toast.LENGTH_LONG).show()
            }
        }

        LiveDataBus.with("livebus_apply_admin").observe(this) {
            val flag = it as Boolean
            if (flag) {
                CustomDialog.Builder2(this@RegisterManagerActivity)
                    .setIcon(R.drawable.ic_alert_wait)
                    .setTitle("已提交申请，请等待后台确认...")
                    .setConfirmText(android.R.string.ok)
                    .setConfirmListener {  }
                    .show()
            } else {
                Toast.makeText(this@RegisterManagerActivity, "失败",Toast.LENGTH_LONG).show()
            }
        }

        binding.editTextTextPersonName.setText(user.phone)
        binding.editTextTextPhone.setText(user.phone)

        binding.buttonLogin.setOnClickListener {
            UserViewModel.loginWithAdmin(lifecycleScope, user.phone!!, true)
        }

        binding.buttonApply.setOnClickListener {
            UserViewModel.applyAdmin(lifecycleScope, user.account!!, user.password!!, "1")
        }

    }

}