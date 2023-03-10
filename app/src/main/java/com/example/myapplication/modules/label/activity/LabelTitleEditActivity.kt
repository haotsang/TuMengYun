package com.example.myapplication.modules.label.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.activity.BaseActivity
import com.example.myapplication.databinding.ActivityLabelTitleEditBinding

class LabelTitleEditActivity: BaseActivity() {

    private lateinit var binding: ActivityLabelTitleEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLabelTitleEditBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.toolbarBack.setOnClickListener { finish() }
        binding.editTextTextTitle.setText(intent.getStringExtra("title"))
        binding.editTextTextContent.setText(intent.getStringExtra("content"))

        binding.buttonOk.setOnClickListener {
            val i = Intent()
            i.putExtra("title", binding.editTextTextTitle.text.toString())
            i.putExtra("content", binding.editTextTextContent.text.toString())
            setResult(3, i)
            finish()
        }
    }
}