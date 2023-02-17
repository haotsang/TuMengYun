package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.adapter.BannerImageAdapter2
import com.example.myapplication.bean.BannerItem
import com.example.myapplication.databinding.ActivityEditLabelBinding
import com.example.myapplication.databinding.ActivityTumengLabelBinding
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator

class TuMengLableActivity: AppCompatActivity() {


    private lateinit var binding: ActivityTumengLabelBinding

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 0 && resultCode == 3) {
            val title = data.getStringExtra("title")
            val content = data.getStringExtra("content")

            binding.banner2Text.text = title + "\n" + content

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTumengLabelBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val imgList = mutableListOf<BannerItem>()

        imgList.add(BannerItem(imagePath = "https://profile.csdnimg.cn/1/7/8/3_weixin_45882303", title = ""))
        imgList.add(BannerItem(imagePath = "https://gw.alicdn.com/bao/uploaded/i3/2210144838464/O1CN01N6ijB52COZ6y9neU6_!!0-item_pic.jpg_210x210q75.jpg_.webp", title = ""))
        imgList.add(BannerItem(imagePath = "https://gw.alicdn.com/imgextra/i2/O1CN01nlPQ9s1y3aKUb5tDo_!!6000000006523-0-tps-800-450.jpg", title = ""))

        binding.banner2.setAdapter(BannerImageAdapter2(imgList, this))
            .addBannerLifecycleObserver(this)
            .setIndicator(CircleIndicator(this))
            .setLoopTime(1500)
            .setOnBannerListener { data, position ->

            }


        binding.buttonAddQuestion.setOnClickListener {
            startActivity(Intent(this, QuestionActivity::class.java))
        }

        binding.buttonAddText.setOnClickListener {
            startActivityForResult(Intent(this, LabelEditActivity::class.java), 0)
        }

        binding.buttonAddImg.setOnClickListener {
//            startActivity()
        }



        binding.buttonUpload.setOnClickListener {
            val content = binding.banner2Text.text.toString()
            val selectId = binding.radioGroup.checkedRadioButtonId == R.id.radio_button1
            val kid = -1
            val question = -1

        }


    }
}