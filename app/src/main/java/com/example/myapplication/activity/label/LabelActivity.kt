package com.example.myapplication.activity.label

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.adapter.BannerImageAdapter2
import com.example.myapplication.databinding.ActivityLabelBinding
import com.example.myapplication.databinding.ViewDialogSinglechoiceBinding
import com.example.myapplication.entity.BannerItem
import com.example.myapplication.entity.LabelBean
import com.example.myapplication.entity.LabelImgBean
import com.example.myapplication.entity.UserBean
import com.example.myapplication.http.HttpUtils
import com.example.myapplication.http.LabelImgUtils
import com.example.myapplication.http.LabelUtils
import com.example.myapplication.http.api.LabelImgApi
import com.example.myapplication.utils.GlideEngine
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.RetrofitUtils
import com.example.myapplication.utils.livebus.LiveDataBus
import com.example.myapplication.view.CustomDialog
import com.google.gson.Gson
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnPageChangeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


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
            submitToCloud()

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
                    positionOffsetPixels: Int,
                ) {}
                override fun onPageSelected(position: Int) {
                }
                override fun onPageScrollStateChanged(state: Int) {}
            })
        binding.banner2Text.setOnClickListener {
            startActivity(Intent(this, QuestionActivity::class.java).apply {
                putExtra("label_id", labelBean?.id)
            })
        }

        var isAll = labelBean?.region == -1
        binding.buttonViewMothed.setOnClickListener {
            val v = ViewDialogSinglechoiceBinding.inflate(LayoutInflater.from(this))
            v.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                isAll = (i == R.id.radio_button1)
            }
            if (isAll) {
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
                    if (isAll) {
                        labelBean?.region = -1
                    } else {
                        labelBean?.region = curRegionId.toInt()
                    }
                }.show()
        }

        binding.buttonAddQuestion.setOnClickListener {
            startActivity(Intent(this, QuestionAddActivity::class.java).apply {
                putExtra("label_id", labelBean?.id)
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
                            val dialog = CustomDialog.Builder2(this@LabelActivity)
                                .setCancelable(false)
                                .setCustomView(ProgressBar(this@LabelActivity))
                                .show()

                            val subList = result.map {
                                BannerItem().apply {
                                    imagePath = it?.path
                                }
                            }
                            labelImgList.addAll(subList)
                            adapter.notifyDataSetChanged()
                            binding.banner2.setCurrentItem(0, true)

                            lifecycleScope.launch(Dispatchers.IO) {
                                for (item in subList) {
                                    try {
                                        val res = LabelImgUtils.uploadImage(labelBean?.id.toString())
                                        val a = Gson().fromJson(res?.data?.toString(), LabelImgBean::class.java)
                                        item.id = a.id
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }

                                withContext(Dispatchers.Main) {
                                    dialog.dismiss()
                                }
                            }

                        }
                    }
                    override fun onCancel() {}
                })
        }

        binding.buttonRemoveImg.setOnClickListener {
            val item = labelImgList.getOrNull(binding.banner2.currentItem)
            if (item != null) {
                val dialog = CustomDialog.Builder2(this@LabelActivity)
                    .setCancelable(false)
                    .setCustomView(ProgressBar(this@LabelActivity))
                    .show()

                lifecycleScope.launch(Dispatchers.IO) {
                    val flag = try {
                        LabelImgUtils.deleteLabelImg(item.imagePath.toString(), item.id.toString())
                    } catch (e: Exception) {
                        false
                    }
                    withContext(Dispatchers.Main) {
                        dialog.dismiss()
                        if (flag) {
                            labelImgList.remove(item)
                            adapter.notifyDataSetChanged()
                            binding.banner2.setCurrentItem(0, true)

                            Toast.makeText(this@LabelActivity, "删除成功", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LabelActivity, "删除失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


        binding.buttonLabelVisible.setOnClickListener {
//            if (labelBean?.visible != 1) {
                labelBean?.visible = 1
            submitToCloud()
//            }
        }
        binding.buttonLabelGone.setOnClickListener {
//            if (labelBean?.visible != 0) {
                labelBean?.visible = 0
            submitToCloud()
//            }
        }

        lifecycleScope.launch(Dispatchers.IO) {

            labelBean = try {
                LabelUtils.getOrInsert(curRegionId)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }
            if (labelBean == null) {
                throw ExceptionInInitializerError("labelBean is null")
            }

            val img = try {
                LabelImgUtils.getLabelImgList(labelBean!!.id.toString())
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
                            this.lid = it.lid
                        }
                    })
                }
                adapter.notifyDataSetChanged()
                binding.banner2.setCurrentItem(0, true)

                updateViewStatus()

            }
        }


    }

    override fun onDestroy() {
        LiveDataBus.send("liveBus_update_label", true)
        super.onDestroy()
    }

    private fun updateViewStatus() {
        binding.banner2Text.text = (labelBean?.title ?: "未设置") + "\n" + (labelBean?.content ?: "未设置")
        binding.labelStatus.text = if (labelBean?.visible == 1) "标签状态：已上传" else "标签状态：已撤销"
        binding.labelStatus.setTextColor(if (labelBean?.visible == 1) ContextCompat.getColor(this@LabelActivity, android.R.color.holo_green_dark) else ContextCompat.getColor(this@LabelActivity, android.R.color.holo_red_dark))
        Prefs.labelSettingItemStatus = labelBean?.visible == 1
    }

    private fun submitToCloud() {
        lifecycleScope.launch(Dispatchers.IO) {
            val state = try {
                LabelUtils.modifyOrInsert(labelBean!!)
            } catch (e: java.lang.Exception) {
                false
            }

            withContext(Dispatchers.Main) {
                if (state) {  //提交成功
                    updateViewStatus()
                }
                Toast.makeText(this@LabelActivity, if (state) "修改成功" else "修改失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

}