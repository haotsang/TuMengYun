package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityIssueBinding
import com.example.myapplication.utils.ViewUtils

class IssueActivity : BaseActivity() {

    private lateinit var binding: ActivityIssueBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setBarsFontLightColor(this, status = true, navigation = true)
        binding = ActivityIssueBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        binding.toolbarTitle.text = "意见反馈"
        binding.toolbarBack.setOnClickListener { finish() }

        binding.button11.setOnClickListener {
            if (binding.editText.text.toString().isNotEmpty()) {
                Toast.makeText(this, "已收到反馈！", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}