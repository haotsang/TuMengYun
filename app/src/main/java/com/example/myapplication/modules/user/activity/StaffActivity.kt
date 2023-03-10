package com.example.myapplication.modules.user.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.activity.BaseActivity
import com.example.myapplication.adapter.KotlinDataAdapter
import com.example.myapplication.databinding.ActivityBaseListBinding
import com.example.myapplication.entity.UserBean
import com.example.myapplication.utils.ViewUtils
import com.example.myapplication.utils.extensions.setOnItemClickListener
import com.example.myapplication.utils.livebus.LiveDataBus
import com.example.myapplication.view.CustomDialog
import com.example.myapplication.viewmodel.UserViewModel


class StaffActivity : BaseActivity() {

    private lateinit var binding: ActivityBaseListBinding

    private val list = mutableListOf<UserBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setBarsFontLightColor(this, status = true, navigation = true)
        binding = ActivityBaseListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.baseTitle.text = "工作人员"
        binding.baseBack.setOnClickListener { finish() }

        val adapter = KotlinDataAdapter.Builder<UserBean>()
            .setLayoutId(R.layout.item_nav)
            .setData(list)
            .addBindView { itemView, itemData ->
                val icon = itemView.findViewById<ImageView>(R.id.item_nav_icon)
                val title = itemView.findViewById<TextView>(R.id.item_nav_title)
                val arrow = itemView.findViewById<ImageView>(R.id.item_nav_arrow)

                val role = when (itemData.role) {
                    0 -> "普通用户"
                    1 -> "工作人员"
                    2 -> "管理员"
                    else -> null
                }
                icon.setImageResource(R.drawable.ic_admin_staff)
                title.text = "${itemData.nickname}@${role}@${(itemData.pin ?: "未知社区/科技馆")}"
            }.create()
        binding.baseRecyclerView.adapter = adapter
        binding.baseRecyclerView.setOnItemClickListener { holder, position ->
            val item = list[position]
            if (item.role == 2) {
                CustomDialog.Builder2(this)
                    .setIcon(R.drawable.ic_alert_error)
                    .setTitle("不可对管理员进行操作")
                    .setConfirmText(android.R.string.ok)
                    .setConfirmListener { }
                    .show()
                return@setOnItemClickListener
            }

            val role = if (item.role == 0) {
                "工作人员"
            } else {
                "普通用户"
            }

            CustomDialog.Builder2(this)
                .setIcon(R.drawable.ic_alert_ask)
                .setTitle("是否将此用户设置为${role}？")
                .setCancelText(android.R.string.cancel)
                .setConfirmText(android.R.string.ok)
                .setCancelListener { }
                .setConfirmListener {
                    when (item.role) {
                        1 -> UserViewModel.modifyRole(lifecycleScope, item.account!!, "0")
                        0 -> UserViewModel.modifyRole(lifecycleScope, item.account!!, "1")
                    }
                }.show()
        }


        LiveDataBus.with("getAllUsers").observe(this) {
            val subList = it as List<UserBean>?
            list.clear()
            if (subList != null) {
                list.addAll(subList)
            }
            binding.baseRecyclerView.adapter?.notifyDataSetChanged()
        }
        UserViewModel.getAllUsers(lifecycleScope)

        LiveDataBus.with("livebus_modify_role").observe(this) {
            val state = it as Boolean
            if (state) {
                Toast.makeText(this@StaffActivity, "设置成功！", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@StaffActivity, "失败", Toast.LENGTH_SHORT).show()
            }
        }


    }
}