package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.entity.UserBean
import com.example.myapplication.databinding.ActivityMineBinding
import com.example.myapplication.http.UserUtils
import com.example.myapplication.utils.Prefs
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMineBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val user: UserBean? = try {
            Gson().fromJson(Prefs.userInfo, UserBean::class.java)
        } catch (e: Exception) {
            null
        }

        if (user == null) {
            binding.textView7.text = "请先登录"
            return
        } else {
            val role = when (user.role) {
                0 -> "普通用户"
                1 -> "工作人员"
                2 -> "管理员"
                else -> null
            }
            binding.textView7.text = "已登录：" + "\n" +
                    "名称：" + user.nickname + "\n" +
                    "账号：" + user.account + "\n" +
                    "角色：" + role + "\n" +
                    "是否在申请管理员：" + if (user.isAdmin == 0) "否" else "是" +
                    "\n\n" + user.toString()
        }

        binding.logout.setOnClickListener {
            Prefs.userInfo = ""
            Prefs.isSaveStatus = false

            finish()
        }

        binding.button14.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val b: Boolean = try {
                    UserUtils.logout(user.account!!, user.password!!)
                } catch (e: Exception) {
                    e.stackTraceToString()
                    false
                }
                withContext(Dispatchers.Main) {
                    if (b) {
                        Prefs.userInfo = ""
                        Prefs.isSaveStatus = false

                        Toast.makeText(this@MineActivity, "注销成功", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@MineActivity, "注销失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}