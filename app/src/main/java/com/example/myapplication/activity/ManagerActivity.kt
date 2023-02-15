package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityManagerBinding

class ManagerActivity: AppCompatActivity() {

    private lateinit var binding: ActivityManagerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.settingsTumengLable.setOnClickListener {
            startActivity(Intent(this, TuMengLableActivity::class.java))
        }
    }
}