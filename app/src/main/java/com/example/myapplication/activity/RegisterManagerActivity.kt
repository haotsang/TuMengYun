package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.entity.UserBean
import com.example.myapplication.databinding.ActivityRegisterManagerBinding
import com.example.myapplication.http.UserUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.view.CustomDialog
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterManagerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.buttonApply.setOnClickListener {
            val user: UserBean? = try {
                Gson().fromJson(Prefs.userInfo, UserBean::class.java)
            } catch (e: Exception) {
                null
            }
            if (user != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val flag = try {
                        UserUtils.applyAdmin(user.account!!, user.password!!, "1")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        false
                    }

                    withContext(Dispatchers.Main) {
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
                }
            }

        }

    }
}