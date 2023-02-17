package com.example.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityEditLabelBinding

class LabelEditActivity: AppCompatActivity() {

    private lateinit var binding: ActivityEditLabelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLabelBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)


        binding.buttonOk.setOnClickListener {
            val i = Intent()
            i.putExtra("title", binding.editTextTextTitle.text.toString())
            i.putExtra("content", binding.editTextTextContent.text.toString())
            setResult(3, i)
            finish()
        }
    }
}