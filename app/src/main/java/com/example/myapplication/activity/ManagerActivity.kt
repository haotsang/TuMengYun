package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.SettingsItem
import com.example.myapplication.databinding.ActivityManagerBinding
import com.example.myapplication.entity.AdminBean
import com.example.myapplication.entity.UserBean
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.ViewUtils
import com.example.myapplication.view.CustomDecoration
import com.google.gson.Gson
import org.json.JSONObject

class ManagerActivity: AppCompatActivity() {

    private lateinit var binding: ActivityManagerBinding

    private val settingsList = mutableListOf(
        SettingsItem("settings_label", "突梦标签", "未设置", R.drawable.ic_nav_register),
        SettingsItem("settings_zhuti", "主题设置", "梦暴科技馆", R.drawable.ic_nav_login),
        SettingsItem("settings_after_service", "售后服务", "", R.drawable.ic_nav_admin),
        SettingsItem("settings_1", "场馆监控", "未添加", R.drawable.ic_nav_clear),
        SettingsItem("settings_2", "数据检测", "", R.drawable.ic_nav_issue),
        SettingsItem("settings_3", "报损", "", R.drawable.ic_nav_group),
        SettingsItem("settings_4", "场馆设置", "", R.drawable.ic_nav_tuisong),
        SettingsItem("settings_5", "供应商信息", "", R.drawable.ic_nav_tuisong),
        SettingsItem("settings_staff", "工作人员", "", R.drawable.ic_nav_tuisong),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setBarsFontLightColor(this, true)

        binding = ActivityManagerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.settingsBack.setOnClickListener { finish() }

        binding.settingsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.settingsRecyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_settings, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }
            override fun getItemCount(): Int = settingsList.size
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = settingsList[holder.adapterPosition]

                val icon = holder.itemView.findViewById<ImageView>(R.id.item_settings_icon)
                val title = holder.itemView.findViewById<TextView>(R.id.item_settings_title)
                val subtitle = holder.itemView.findViewById<TextView>(R.id.item_settings_subtitle)

                icon.setImageResource(item.icon)
                title.text = item.title
                subtitle.text = item.subtitle

                holder.itemView.setOnClickListener {
                    when (item.id) {
                        "settings_label" -> {
                            startActivity(Intent(this@ManagerActivity, LabelActivity::class.java))
                        }
                        "settings_zhuti" -> {
                            startActivity(Intent(this@ManagerActivity, AdminActivity::class.java))
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
                    }
                }
            }
        }
        val itemDecoration = CustomDecoration(
            this, (binding.settingsRecyclerView.layoutManager as LinearLayoutManager)
                .orientation, false
        )
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.color.ripper)!!)
        binding.settingsRecyclerView.addItemDecoration(itemDecoration)
    }

    override fun onResume() {
        super.onResume()


        settingsList.getOrNull(0)?.subtitle = if (Prefs.labelSettingItemStatus) "已设置" else "未设置"
        binding.settingsRecyclerView.adapter?.notifyItemChanged(0)

        val admin: AdminBean? = try {
            Gson().fromJson(Prefs.adminInfo, AdminBean::class.java)
        } catch (e: Exception) {
            null
        }
        if (admin != null) {
            settingsList.getOrNull(1)?.subtitle = admin.name!!
            binding.settingsRecyclerView.adapter?.notifyItemChanged(1)
        }

    }
}