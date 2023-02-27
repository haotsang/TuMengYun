package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.adapter.BannerImageAdapter2
import com.example.myapplication.entity.BannerItem
import com.example.myapplication.entity.LabelBean
import com.example.myapplication.databinding.ActivityLabelBinding
import com.example.myapplication.entity.UserBean
import com.example.myapplication.http.LabelImgUtils
import com.example.myapplication.http.LabelUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.ViewUtils
import com.google.gson.Gson
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnPageChangeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LabelActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLabelBinding

    private var labelBean: LabelBean? = null

    private lateinit var adapter: BannerImageAdapter2
    private val labelImgList = mutableListOf<BannerItem>()
    private var curIndex = -1

    private val selectPictureLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            val url = uri.toString()
            labelImgList.add(BannerItem().apply {
                imagePath = url
            })
            adapter.notifyDataSetChanged()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 0 && resultCode == 3) {
            val title = data.getStringExtra("title")
            val content = data.getStringExtra("content")
            labelBean?.title = title
            labelBean?.content = content

            binding.banner2Text.text = (labelBean?.title ?: "未设置") + "\n" + (labelBean?.content ?: "未设置")
            updateToCloud()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLabelBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.labelToolbarBack.setOnClickListener { finish() }

        val user: UserBean? = try {
            Gson().fromJson(Prefs.userInfo, UserBean::class.java)
        } catch (e: Exception) {
            null
        }

        if (user == null) {
            Toast.makeText(this, "未登录，请先登录", Toast.LENGTH_SHORT).show()
            return
        }
        val curRegionId = user.belong.toString()

        adapter = BannerImageAdapter2(labelImgList, this)
        binding.banner2.setAdapter(adapter)
            .addBannerLifecycleObserver(this)
            .setIndicator(CircleIndicator(this))
            .setLoopTime(1500)
            .addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {}
                override fun onPageSelected(position: Int) {
                    curIndex = position
                    println("++++++++++++++++++++++++++++++++++${curIndex}")
                }
                override fun onPageScrollStateChanged(state: Int) {}
            })
            .setOnBannerListener { data, position ->
            }

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            if (i == R.id.radio_button1) {
                labelBean?.type = 1  //全部系统
            } else {
                labelBean?.type = 0
            }

//            updateToCloud()
        }

        binding.buttonAddQuestion.setOnClickListener {
            startActivity(Intent(this, QuestionActivity::class.java).apply {
                putExtra("region", curRegionId)
            })
        }

        binding.buttonAddText.setOnClickListener {
            startActivityForResult(
                Intent(this, LabelTitleEditActivity::class.java).apply {
                    putExtra("title", labelBean?.title)
                    putExtra("content", labelBean?.content)
                }, 0
            )
        }

        binding.buttonAddImg.setOnClickListener {
            try {
                selectPictureLauncher.launch(arrayOf("image/*"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.buttonRemoveImg.setOnClickListener {
//            val curIndex = (binding.banner2.currentItem) ?: -1

            if (curIndex > 0) {
                labelImgList.removeAt(curIndex)
                adapter.notifyDataSetChanged()
            } else {
                labelImgList.clear()
                adapter.notifyDataSetChanged()
            }

            println("@@@@@@@@@@@@@@@@@@@@@@@@@@   ${curIndex}")
        }


        binding.buttonLabelVisible.setOnClickListener {
//            if (labelBean?.visible != 1) {
                labelBean?.visible = 1
                updateToCloud()
//            }
        }
        binding.buttonLabelGone.setOnClickListener {
//            if (labelBean?.visible != 0) {
                labelBean?.visible = 0
                updateToCloud()
//            }
        }


//        binding.buttonUpload.setOnClickListener {
//            val content = binding.banner2Text.text.toString()
//            val selectId = binding.radioGroup.checkedRadioButtonId == R.id.radio_button1
//            val kid = -1
//            val question = -1
//
//        }


        lifecycleScope.launch(Dispatchers.IO) {
            val img = try {
                LabelImgUtils.getLabelImgList(curRegionId)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }

            labelBean = try {
                LabelUtils.getLabel(curRegionId)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }

            withContext(Dispatchers.Main) {
                labelImgList.clear()
                if (img != null) {
                    labelImgList.addAll(img.map {
                        BannerItem().apply {
                            this.id = it.id
                            this.imagePath = it.uri
                            this.order = it.tid
                        }
                    })
                }
                adapter.notifyDataSetChanged()


                if (labelBean != null) {
                    binding.banner2Text.text = (labelBean?.title ?: "未设置") + "\n" + (labelBean?.content ?: "未设置")
                    binding.labelStatus.text = if (labelBean?.visible == 1) "标签状态：已上传" else "标签状态：已撤销"
                    binding.labelStatus.setTextColor(if (labelBean?.visible == 1) ContextCompat.getColor(this@LabelActivity, android.R.color.holo_green_dark) else ContextCompat.getColor(this@LabelActivity, android.R.color.holo_red_dark))

                    binding.radioGroup.check(if (labelBean?.type == 1) R.id.radio_button1 else R.id.radio_button2)

                    Prefs.labelSettingItemStatus = labelBean?.visible == 1
                } else {
                    labelBean = LabelBean().apply {
                        this.region = curRegionId.toInt()
                        this.type = 0
                        this.visible = 0
                    }
                }
            }
        }


    }

    private fun updateToCloud() {
        lifecycleScope.launch(Dispatchers.IO) {
            val state = if (labelBean != null) {
                try {
                    LabelUtils.modify(labelBean!!)
                } catch (e: java.lang.Exception) {
                    false
                }
            } else false

            withContext(Dispatchers.Main) {
                if (state) {  //提交成功
                    Prefs.labelSettingItemStatus = labelBean?.visible == 1
                    binding.labelStatus.text = if (labelBean?.visible == 1) "标签状态：已上传" else "标签状态：已撤销"
                    binding.labelStatus.setTextColor(if (labelBean?.visible == 1) ContextCompat.getColor(this@LabelActivity, android.R.color.holo_green_dark) else ContextCompat.getColor(this@LabelActivity, android.R.color.holo_red_dark))
                }

                Toast.makeText(this@LabelActivity, if (state) "修改成功" else "修改失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

}