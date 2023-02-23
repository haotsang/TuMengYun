package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.bean.UserBean
import com.example.myapplication.databinding.ActivityMineBinding
import com.example.myapplication.utils.http.LoginUtils
import com.example.myapplication.utils.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMineBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        if (Prefs.isLogin && Prefs.userAccount.isNotEmpty() && Prefs.userPassword.isNotEmpty()) {
            lifecycleScope.launch(Dispatchers.IO) {
                val status: UserBean? = try {
                    LoginUtils.getUser(Prefs.userAccount, Prefs.userPassword)
                } catch (e: Exception) {
                    e.stackTraceToString()
                    null
                }
                withContext(Dispatchers.Main) {
                    if (status != null) {
                        val role = when (status.role) {
                            0 -> "普通用户"
                            1 -> "工作人员"
                            2 -> "管理员"
                            else -> null
                        }
                        binding.textView7.text = "已登录：" + "\n" +
                                "名称：" + status.nickname + "\n" +
                                "账号：" + status.account + "\n" +
                                "角色：" + role +"\n" +
                                "是否在申请管理员：" + if (status.isAdmin==0)"否" else "是"
                    }
                }
            }

        }

        binding.logout.setOnClickListener {
            Prefs.isLogin = false
            Prefs.userAccount = ""
            Prefs.userPassword = ""
            Prefs.isSaveStatus = false

            finish()
        }

        binding.button14.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val b: Boolean = try {
                    LoginUtils.logout(Prefs.userAccount, Prefs.userPassword)
                } catch (e: Exception) {
                    e.stackTraceToString()
                    false
                }
                withContext(Dispatchers.Main) {
                    if (b) {
                        Prefs.isLogin = false
                        Prefs.userAccount = ""
                        Prefs.userPassword = ""
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