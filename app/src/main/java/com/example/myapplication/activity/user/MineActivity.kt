package com.example.myapplication.activity.user

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMineBinding
import com.example.myapplication.entity.UserRewardBean
import com.example.myapplication.http.UserRewardUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.livebus.LiveDataBus
import com.example.myapplication.view.CustomDialog
import com.example.myapplication.viewmodel.UserViewModel
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

        val user = UserViewModel.user


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
            val s = "已登录：" + "\n" +
                    "名称：" + user.nickname + "\n" +
                    "账号：" + user.account + "\n" +
                    "角色：" + role + "\n" +
                    "是否在申请管理员：" + if (user.isApply == 0) "否" else "是" +
                    "申請時間：" + user.applyTime.toString()
                    "\n\n" + user.toString() +

                    "\n\n" + UserViewModel.region.toString() +
                    "\n\n" + "积分：{@}"
            binding.textView7.text = s


            lifecycleScope.launch(Dispatchers.IO) {
                val reward = try {
                    UserRewardUtils.getReward(
                        Gson().toJson(
                            UserRewardBean().apply {
                                this.uid = UserViewModel.user?.id
                                this.pin = UserViewModel.region?.pin
                            }
                        )
                    )
                } catch (e:Exception){
                    e.printStackTrace()
                    0
                }
                withContext(Dispatchers.Main){
                    val s2 = s.replace("{@}", "" + reward)
                    binding.textView7.text = s2
                    Toast.makeText(this@MineActivity, "{${reward}}", Toast.LENGTH_SHORT).show()
                }

            }

        }


        LiveDataBus.with("livebus_login").observe(this) {
            val pair = it as Pair<Boolean, String>
            if (pair.first) {
                Toast.makeText(this@MineActivity, "注销成功", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@MineActivity, "注销失败", Toast.LENGTH_SHORT).show()
            }
        }

        binding.logout.setOnClickListener {
            Prefs.userInfo = ""
            Prefs.isSaveStatus = false

            UserViewModel.user = null
            finish()
        }

        binding.button14.setOnClickListener {
            CustomDialog.Builder2(this)
                .setIcon(R.drawable.ic_alert_ask)
                .setTitle("将删除此账户所有数据，包括已获得的积分，是否继续？")
                .setCancelListener {  }
                .setConfirmListener {
                    UserViewModel.logout(lifecycleScope, user.account!!, user.password!!)
                }
                .show()

        }

    }
}