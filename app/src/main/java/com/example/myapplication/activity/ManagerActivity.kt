package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityManagerBinding
import com.example.myapplication.utils.Prefs
import org.json.JSONObject

class ManagerActivity: AppCompatActivity() {

    private lateinit var binding: ActivityManagerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.settingsTumengLable.setOnClickListener {
            startActivity(Intent(this, LableActivity::class.java))
        }
        binding.settingsItemsAdmin.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }

        binding.settingsItemStaff.setOnClickListener {
            startActivity(Intent(this, StaffActivity::class.java))
        }

        binding.settingsItemAfterService.setOnClickListener {
            startActivity(Intent(this, UploadShouHouActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.settingsTumengLableStatus.text = if (Prefs.labelSettingItemStatus) "已设置" else "未设置"
        binding.settingsItemAdminStatus.text = JSONObject(Prefs.curRegionId).optString("name")
    }
}