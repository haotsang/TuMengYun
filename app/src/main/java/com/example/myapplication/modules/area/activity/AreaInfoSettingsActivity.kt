package com.example.myapplication.modules.area.activity

import android.content.Intent
import android.os.Bundle
import com.example.myapplication.activity.BaseActivity
import com.example.myapplication.databinding.ActivityBaseListBinding
import com.example.myapplication.utils.ViewUtils

class AreaInfoSettingsActivity : BaseActivity() {

    private lateinit var binding: ActivityBaseListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setBarsFontLightColor(this, status = true, navigation = true)
        binding = ActivityBaseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.baseTitle.text = "场馆设置"
        binding.baseBack.setOnClickListener { finish() }

        binding.baseOverflow.text = "添加展品"
        binding.baseOverflow.setOnClickListener {
            startActivity(Intent(this, AddExhibitActivity::class.java))
        }

    }
}