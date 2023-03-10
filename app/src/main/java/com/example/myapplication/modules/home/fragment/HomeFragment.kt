package com.example.myapplication.modules.home.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.modules.label.activity.QuestionActivity
import com.example.myapplication.adapter.BannerImageAdapter2
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.entity.*
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.livebus.LiveDataBus
import com.example.myapplication.viewmodel.LabelViewModel
import com.example.myapplication.viewmodel.UserViewModel
import com.youth.banner.indicator.CircleIndicator
import java.io.File

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    private val labelImgList = mutableListOf<BannerItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initMainCard()

        LiveDataBus.with("livebus_user_change").observe(this) {
            LabelViewModel.getLabel(lifecycleScope, UserViewModel.region?.pin!!)
            LabelViewModel.getLabelImg(lifecycleScope, LabelViewModel.label?.id?.toString())

            val user = it as UserBean?
            binding?.cardItem4?.cardTips?.text = "积分数量"
            binding?.cardItem4?.cardValue?.text = "${user?.reward ?: 0}分"

            println("#####  livebus_user_change")
        }
        LiveDataBus.with("livebus_region_change").observe(this) {
            LabelViewModel.getLabel(lifecycleScope, UserViewModel.region?.pin!!)
            LabelViewModel.getLabelImg(lifecycleScope, LabelViewModel.label?.id?.toString())

            val region = it as RegionBean?

//            binding?.contentTitle?.text = region?.name ?: ""
            println("#####  livebus_region_change")
        }

        LiveDataBus.with("livebus_label_change").observe(this) {
            val labelBean = it as LabelBean?
            if (labelBean != null) {
                binding?.bannerText?.text = if (labelBean.visible == 1) {
                    (labelBean.title ?: "未设置") + "\n" + (labelBean.content ?: "未设置")
                } else "标签未上传"
            } else {
                binding?.bannerText?.text = "empty"
            }

            println("#####  livebus_label_change")
        }
        LiveDataBus.with("livebus_label_img_change").observe(this) {
            val imgList = it as List<LabelImgBean>?

            val img = if (imgList != null && LabelViewModel.label != null && LabelViewModel.label?.visible == 1) {
                imgList
            } else listOf(LabelImgBean())  //占位图

            labelImgList.clear()
            labelImgList.addAll(img.map {
                BannerItem().apply {
                    this.id = it.id
                    this.imagePath = it.uri
                    this.lid = it.lid
                }
            })
            binding?.banner?.setDatas(labelImgList)

            println("#####  livebus_label_img_change  ${imgList?.size}")
        }

        UserViewModel.verify(lifecycleScope)

    }

    private fun initView() {
        binding?.banner!!.setAdapter(BannerImageAdapter2(labelImgList, requireContext()))
            .addBannerLifecycleObserver(this)
            .setIndicator(CircleIndicator(requireContext()))
            .setLoopTime(1500)
            .setOnBannerListener { data, position ->
                if (UserViewModel.user != null && LabelViewModel.label != null && LabelViewModel.label?.visible == 1) {
                    startActivity(Intent(requireContext(), QuestionActivity::class.java).apply {
                        putExtra("label_id", LabelViewModel.label?.id)
                    })
                }
            }
    }

    private fun initMainCard() {
        binding?.cardItem1?.cardImg?.setImageResource(R.drawable.ic_card_flow)
        binding?.cardItem1?.cardTips?.text = "客流"
        binding?.cardItem1?.cardValue?.text = "200人"

        binding?.cardItem2?.cardImg?.setImageResource(R.drawable.ic_card_power)
        binding?.cardItem2?.cardTips?.text = "能源消耗"
        binding?.cardItem2?.cardValue?.text = "20°电"

        binding?.cardItem3?.cardImg?.setImageResource(R.drawable.ic_card_damage)
        binding?.cardItem3?.cardTips?.text = "设备损坏"
        binding?.cardItem3?.cardValue?.text = "2件"

        binding?.cardItem4?.cardImg?.setImageResource(R.drawable.ic_card_staff)
        binding?.cardItem4?.cardTips?.text = "工作人员"
        binding?.cardItem4?.cardValue?.text = "30人"

    }

    override fun onDestroyView() {
        binding = null

        if (!Prefs.isSaveStatus) {
            Prefs.userInfo = ""
        }

        val user = UserViewModel.user
        if (user != null) {
            val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "${user.id}.questions")
            if (file.exists()) file.delete()
        }

        super.onDestroyView()
    }
}