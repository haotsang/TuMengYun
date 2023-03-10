package com.example.myapplication.modules.label.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.activity.BaseActivity
import com.example.myapplication.adapter.BannerImageAdapter2
import com.example.myapplication.databinding.ActivityLabelBinding
import com.example.myapplication.databinding.ViewDialogSinglechoiceBinding
import com.example.myapplication.entity.BannerItem
import com.example.myapplication.entity.LabelImgBean
import com.example.myapplication.http.LabelImgUtils
import com.example.myapplication.http.LabelUtils
import com.example.myapplication.utils.*
import com.example.myapplication.utils.livebus.LiveDataBus
import com.example.myapplication.view.CustomDialog
import com.example.myapplication.viewmodel.LabelViewModel
import com.example.myapplication.viewmodel.UserViewModel
import com.google.gson.Gson
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnPageChangeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class LabelActivity: BaseActivity() {

    private lateinit var binding: ActivityLabelBinding

    private lateinit var adapter: BannerImageAdapter2
    private val labelImgList = mutableListOf<BannerItem>()

    private var startTime: Date? = null
    private var endTime: Date? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 0 && resultCode == 3) {
            val title = data.getStringExtra("title")
            val content = data.getStringExtra("content")
            LabelViewModel.label?.title = title
            LabelViewModel.label?.content = content

            binding.banner2Text.text = (LabelViewModel.label?.title ?: "?????????") + "\n" + (LabelViewModel.label?.content ?: "?????????")
            submitToCloud()

        } else if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            try {
                val dialog = CustomDialog.Builder2(this@LabelActivity)
                    .setCancelable(false)
                    .setCustomView(ProgressBar(this@LabelActivity))
                    .show()

                lifecycleScope.launch(Dispatchers.IO) {
                    val input = contentResolver.openInputStream(uri)
                    val file = File(
                        getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        System.currentTimeMillis().toString()
                    )
                    val path = JavaHelper.streamToFile(input, file)
                    input?.close()

                    val res = try {
                        LabelImgUtils.uploadImage(
                            File(path),
                            LabelViewModel.label?.id.toString()
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }

                    withContext(Dispatchers.Main) {
                        dialog.dismiss()

                        if (res != null) {
                            val data = Gson().fromJson(Gson().toJson(res.data), LabelImgBean::class.java)

                            val item = BannerItem()
                            item.id = data.id
                            item.imagePath = data.uri
                            item.lid = data.lid

                            labelImgList.add(item)
                            binding.banner2.setDatas(labelImgList)
                            Toast.makeText(this@LabelActivity, "??????", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LabelActivity, "??????", Toast.LENGTH_SHORT).show()
                        }

                    }

                }


            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLabelBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.labelToolbarBack.setOnClickListener { finish() }

        if (UserViewModel.user == null) {
            Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show()
            return
        }

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

        var isAll = false//labelBean?.pin == -1
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
//                        labelBean?.pin = -1
                    } else {
//                        labelBean?.pin = curRegionId.toInt()
                    }
                }.show()
        }

        binding.buttonQuestionList.setOnClickListener {
            startActivity(Intent(this, QuestionListActivity::class.java).apply {
                putExtra("label_id", LabelViewModel.label?.id)
            })
        }

        binding.buttonSetStartTime.setOnClickListener {
            CardDatePickerDialog.Builder(this)
                .setDefaultTime((LabelViewModel.label!!.startTime?.time) ?: System.currentTimeMillis())
                .setTitle("??????????????????")
                .setOnChoose {
                    startTime = Date(it)
                    LabelViewModel.label!!.startTime = startTime

                    submitToCloud()
                }.build().show()
        }
        binding.buttonSetEndTime.setOnClickListener {
            CardDatePickerDialog.Builder(this)
                .setDefaultTime((LabelViewModel.label!!.endTime?.time) ?: System.currentTimeMillis())
                .setTitle("??????????????????").setOnChoose {
                    if (startTime != null && startTime!!.time < it) {
                        endTime = Date(it)
                        LabelViewModel.label!!.endTime = endTime

                        submitToCloud()
                    } else {
                        CustomDialog.Builder2(this)
                            .setIcon(R.drawable.ic_alert_ask)
                            .setTitle("??????????????????????????????????????????")
                            .setConfirmListener { }
                            .show()
                    }
                }.build().show()
        }


        binding.buttonAddText.setOnClickListener {
            startActivityForResult(
                Intent(this, LabelTitleEditActivity::class.java).apply {
                    putExtra("title", LabelViewModel.label?.title)
                    putExtra("content", LabelViewModel.label?.content)
                }, 0
            )
        }

        binding.buttonAddImg.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, 101)


            PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setMaxSelectNum(3)
                .setImageEngine(GlideEngine.createGlideEngine())
                .setSandboxFileEngine(MeSandboxFileEngine())
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>?) {
                        if (result != null) {
                            val dialog = CustomDialog.Builder2(this@LabelActivity)
                                .setCancelable(false)
                                .setCustomView(ProgressBar(this@LabelActivity))
                                .show()

                            lifecycleScope.launch(Dispatchers.IO) {
                                val subList = result.map {
                                    val realPath = if (Build.VERSION.SDK_INT >= 29) {
                                        try {
                                            val input =
                                                contentResolver.openInputStream(Uri.parse(it?.path!!))
                                            val file = File(
                                                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                                System.currentTimeMillis().toString()
                                            )
                                            val p = JavaHelper.streamToFile(input, file)
                                            input?.close()

                                            p
                                        } catch (e: IOException) {
                                            e.printStackTrace()
                                            ""
                                        }
                                    } else it?.path


                                    BannerItem().apply {
                                        imagePath = realPath
                                    }
                                }


                                for (item in subList) {
                                    try {
                                        val res = try {
                                            LabelImgUtils.uploadImage(
                                                File(item.imagePath!!),
                                                LabelViewModel.label?.id.toString()
                                            )
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            null
                                        }

                                        if (res != null) {
                                            val data = Gson().fromJson(Gson().toJson(res.data), LabelImgBean::class.java)
                                            item.id = data.id
                                            item.imagePath = data.uri
                                            item.lid = data.lid
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }

                                withContext(Dispatchers.Main) {
                                    labelImgList.addAll(subList)
                                    binding.banner2.setDatas(labelImgList)

                                    println(labelImgList.joinToString("\n"))
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
                            binding.banner2.setDatas(labelImgList)

                            Toast.makeText(this@LabelActivity, "????????????", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LabelActivity, "????????????", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


        binding.buttonLabelVisible.setOnClickListener {
//            if (labelBean?.visible != 1) {
            LabelViewModel.label?.visible = 1
            submitToCloud()
//            }
        }
        binding.buttonLabelGone.setOnClickListener {
//            if (labelBean?.visible != 0) {
            LabelViewModel.label?.visible = 0
            submitToCloud()
//            }
        }



        LiveDataBus.with("livebus_label_change2").observe(this) {
            updateViewStatus()
        }
        LiveDataBus.with("livebus_label_img_change2").observe(this) {
            val imgList = it as List<LabelImgBean>?

            labelImgList.clear()
            if (imgList != null) {
                labelImgList.addAll(imgList.map {
                    BannerItem().apply {
                        this.id = it.id
                        this.imagePath = it.uri
                        this.lid = it.lid
                    }
                })
            }
            binding.banner2.setDatas(labelImgList)
        }

        LabelViewModel.getLabel2(lifecycleScope, UserViewModel.region?.pin!!)
        LabelViewModel.getLabelImg2(lifecycleScope, LabelViewModel.label?.id?.toString())


    }

    override fun onPause() {
        super.onPause()
        LabelViewModel.getLabel(lifecycleScope, UserViewModel.region?.pin!!)
        LabelViewModel.getLabelImg(lifecycleScope, LabelViewModel.label?.id?.toString())
    }

    private fun updateViewStatus() {
        binding.banner2Text.text = (LabelViewModel.label?.title ?: "?????????") + "\n" + (LabelViewModel.label?.content ?: "?????????")
        binding.labelStatus.text = if (LabelViewModel.label?.visible == 1) "????????????????????????" else "????????????????????????"
        binding.labelStatus.setTextColor(if (LabelViewModel.label?.visible == 1) ContextCompat.getColor(this@LabelActivity, android.R.color.holo_green_dark) else ContextCompat.getColor(this@LabelActivity, android.R.color.holo_red_dark))
        Prefs.labelSettingItemStatus = LabelViewModel.label?.visible == 1
    }

    private fun submitToCloud() {
        lifecycleScope.launch(Dispatchers.IO) {
            val state = try {
                LabelUtils.modifyOrInsert(LabelViewModel.label!!)
            } catch (e: java.lang.Exception) {
                false
            }

            withContext(Dispatchers.Main) {
                if (state) {  //????????????
                    updateViewStatus()
                }
                Toast.makeText(this@LabelActivity, if (state) "????????????" else "????????????", Toast.LENGTH_SHORT).show()
            }
        }
    }

}