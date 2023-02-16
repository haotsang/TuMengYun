package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityIssueBinding

class IssueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIssueBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIssueBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        binding.toolbar.title = "意见反馈"
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.button11.setOnClickListener {
            Toast.makeText(this, "已收到反馈！", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}