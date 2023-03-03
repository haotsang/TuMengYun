package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.entity.UserBean
import com.example.myapplication.databinding.ActivityRegisterManagerBinding
import com.example.myapplication.entity.ResponseBase
import com.example.myapplication.http.UserUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.livebus.LiveDataBus
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

        val user: UserBean? = try {
            Gson().fromJson(Prefs.userInfo, UserBean::class.java)
        } catch (e: Exception) {
            null
        }
        if (user == null) {
            Toast.makeText(this, "未登录，请先登录", Toast.LENGTH_SHORT).show()
            return
        }

        binding.editTextTextPersonName.setText(user.phone)
        binding.editTextTextPhone.setText(user.phone)

        binding.buttonLogin.setOnClickListener {
            loginWithPhone(user.phone!!, true)
        }

        binding.buttonApply.setOnClickListener {
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

    private fun loginWithPhone(phone: String, saveState: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val responseBase: ResponseBase? = try {
                UserUtils.loginWithPhone(phone)
            } catch (e: Exception) {
                null
            }
            withContext(Dispatchers.Main) {
                if (responseBase != null && responseBase.code == 200) {
                    val newUser = Gson().fromJson(Gson().toJson(responseBase.data), UserBean::class.java)
                    if (newUser.role == 2) {
                        Prefs.isSaveStatus = saveState
                        Prefs.userInfo = Gson().toJson(responseBase.data)
                        Prefs.isLoginFromPhone = true

                        LiveDataBus.send("liveBus_update_info", true)
                        LiveDataBus.send("liveBus_update_label", true)
                        Toast.makeText(this@RegisterManagerActivity, "管理员登录成功！", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this@RegisterManagerActivity, "您不是管理员，请重新申请或等待系统却认", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Prefs.isSaveStatus = false
                    Prefs.userInfo = ""
                    Toast.makeText(
                        this@RegisterManagerActivity,
                        "登录失败，${responseBase?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}