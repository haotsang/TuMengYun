package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.modules.label.activity.LabelActivity
import com.example.myapplication.modules.user.activity.RegionActivity
import com.example.myapplication.modules.user.activity.StaffActivity
import com.example.myapplication.adapter.KotlinDataAdapter
import com.example.myapplication.databinding.ActivityBaseListBinding
import com.example.myapplication.entity.SettingsItem
import com.example.myapplication.modules.area.activity.ExhibitListActivity
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.ViewUtils
import com.example.myapplication.utils.extensions.setOnItemClickListener
import com.example.myapplication.viewmodel.UserViewModel

class ManagerActivity: BaseActivity() {

    private lateinit var binding: ActivityBaseListBinding

    private val settingsList = mutableListOf(
        SettingsItem("settings_label", "突梦标签", "未设置", R.drawable.ic_admin_label),
        SettingsItem("settings_zhuti", "主题设置", "梦暴科技馆", R.drawable.ic_admin_style),
        SettingsItem("settings_after_service", "售后服务", "", R.drawable.ic_admin_service),
        SettingsItem("settings_1", "场馆监控", "未添加", R.drawable.ic_admin_jiankong),
        SettingsItem("settings_2", "数据检测", "", R.drawable.ic_admin_monitor),
        SettingsItem("settings_3", "报损", "", R.drawable.ic_admin_baosun),
        SettingsItem("settings_cg", "场馆设置", "", R.drawable.ic_admin_pos_setting),
        SettingsItem("settings_5", "供应商信息", "", R.drawable.ic_admin_supply),
        SettingsItem("settings_staff", "工作人员", "", R.drawable.ic_admin_staff),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setBarsFontLightColor(this, status = true, navigation = true)

        binding = ActivityBaseListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.baseBack.setOnClickListener { finish() }
        binding.baseTitle.text = "智慧管理"

        val adapter = KotlinDataAdapter.Builder<SettingsItem>()
            .setLayoutId(R.layout.item_settings)
            .setData(settingsList)
            .addBindView { itemView, itemData ->
                val icon = itemView.findViewById<ImageView>(R.id.item_settings_icon)
                val title = itemView.findViewById<TextView>(R.id.item_settings_title)
                val subtitle = itemView.findViewById<TextView>(R.id.item_settings_subtitle)

                icon.setImageResource(itemData.icon)
                title.text = itemData.title
                subtitle.text = itemData.subtitle
            }.create()
        binding.baseRecyclerView.adapter = adapter
        binding.baseRecyclerView.setOnItemClickListener { holder, position ->
            val item = settingsList[position]
            when (item.id) {
                "settings_label" -> {
                    startActivity(Intent(this@ManagerActivity, LabelActivity::class.java))
                }
                "settings_zhuti" -> {
                    startActivity(Intent(this@ManagerActivity, RegionActivity::class.java))
                }
                "settings_after_service" -> startActivity(
                    Intent(
                        this@ManagerActivity,
                        AfterServiceActivity::class.java
                    )
                )
                "settings_staff" -> {
                    startActivity(Intent(this@ManagerActivity, StaffActivity::class.java))
                }
                "settings_cg" -> {
                    startActivity(Intent(this@ManagerActivity, ExhibitListActivity::class.java))
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()


        settingsList.getOrNull(0)?.subtitle = if (Prefs.labelSettingItemStatus) "已设置" else "未设置"
        binding.baseRecyclerView.adapter?.notifyItemChanged(0)


        if (UserViewModel.region != null) {
            settingsList.getOrNull(1)?.subtitle = UserViewModel.region?.name ?: ""
            binding.baseRecyclerView.adapter?.notifyItemChanged(1)
        }

    }
}