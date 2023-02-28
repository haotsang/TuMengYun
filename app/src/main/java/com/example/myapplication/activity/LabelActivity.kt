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
import com.example.myapplication.databinding.ActivityLabelBinding
import com.example.myapplication.databinding.ViewDialogSinglechoiceBinding
import com.example.myapplication.entity.BannerItem
import com.example.myapplication.entity.LabelBean
import com.example.myapplication.entity.UserBean
import com.example.myapplication.http.LabelImgUtils
import com.example.myapplication.http.LabelUtils
import com.example.myapplication.utils.GlideEngine
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.ViewUtils
import com.example.myapplication.view.CustomDialog
import com.google.gson.Gson
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.style.PictureSelectorStyle
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnPageChangeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LabelActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLabelBinding

    private var labelBean: LabelBean? = null

    private lateinit var adapter: BannerImageAdapter2
    private val labelImgList = mutableListOf<BannerItem>()


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

//        if (user == null) {
//            Toast.makeText(this, "未登录，请先登录", Toast.LENGTH_SHORT).show()
//            return
//        }
        val curRegionId = user?.belong.toString()

        adapter = BannerImageAdapter2(labelImgList, this)
        binding.banner2.setAdapter(adapter)
            .addBannerLifecycleObserver(this)
            .setIndicator(CircleIndicator(this))
            .setLoopTime(1500)
            .addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {}
                override fun onPageSelected(position: Int) {}
                override fun onPageScrollStateChanged(state: Int) {}
            })
            .setOnBannerListener { data, position ->
                startActivity(Intent(this, QuestionActivity::class.java).apply {
                    putExtra("region", curRegionId)
                })
            }

        var typeState = labelBean?.type
        binding.buttonViewMothed.setOnClickListener {
            val v = ViewDialogSinglechoiceBinding.inflate(LayoutInflater.from(this))
            v.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                typeState = if (i == R.id.radio_button1) {
                    1 //全部系统
                } else {
                    0
                }
            }
            if (typeState == 1) {
                v.radioGroup.check(R.id.radio_button1)
            } else {
                v.radioGroup.check(R.id.radio_button2)
            }
            CustomDialog.Builder2(this@LabelActivity)
                .setCustomView(v.root)
                .setCancelText(android.R.string.cancel)
                .setConfirmText(android.R.string.ok)
                .setCancelListener {  }
                .setConfirmListener {
                    if (typeState != null) {
                        labelBean?.type = typeState!!
                    }
                }.show()
        }

        binding.buttonAddQuestion.setOnClickListener {
            startActivity(Intent(this, QuestionAddActivity::class.java).apply {
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
            PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setMaxSelectNum(3)
                .setImageEngine(GlideEngine.createGlideEngine())
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>?) {
                        if (result != null) {
                            if (labelImgList.size < 3) {
                                for (i in 0 until result.size) {
                                    val item = BannerItem().apply {
                                        imagePath = result.getOrNull(i)?.path
                                    }
                                    labelImgList.add(item)
                                }
                                adapter.notifyDataSetChanged()
                            } else {
                                Toast.makeText(this@LabelActivity, "你已选择3张图片，请移除后重新选择", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    override fun onCancel() {}
                })
        }

        binding.buttonRemoveImg.setOnClickListener {
            val i = if (binding.banner2.currentItem == 0) 0 else binding.banner2.currentItem - 1
            if (labelImgList.size > 0) {
                labelImgList.removeAt(i)
                adapter.notifyDataSetChanged()
            }
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

/*
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

*/
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