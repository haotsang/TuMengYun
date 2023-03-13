package com.example.myapplication.modules.shop.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.adapter.BannerImageAdapter2
import com.example.myapplication.adapter.KotlinDataAdapter
import com.example.myapplication.databinding.ActivityBaseListBinding
import com.example.myapplication.databinding.FragmentInfoBinding
import com.example.myapplication.modules.label.activity.QuestionActivity
import com.example.myapplication.viewmodel.LabelViewModel
import com.example.myapplication.viewmodel.UserViewModel
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator

class LocationFragment : Fragment() {

    private var binding: FragmentInfoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.baseBack?.setImageResource(0)
        binding?.baseTitle?.text = "梦暴科技馆"


        binding?.baseOverflow?.text = "VR全景"

        initView()


    }

    private fun initView() {
        binding?.banner!!.setAdapter(object : BannerImageAdapter<String>(arrayListOf("","","")) {
            override fun onBindView(
                holder: BannerImageHolder?,
                data: String?,
                position: Int,
                size: Int
            ) {
                if (holder == null) {
                    return
                }
                Glide.with(holder.itemView)
                    .load(R.drawable.aa)
                    .placeholder(R.drawable.bg)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(holder.imageView)
            }

        }).addBannerLifecycleObserver(this)
            .setIndicator(CircleIndicator(requireContext()))
            .setLoopTime(1500)
            .setOnBannerListener { data, position ->

            }

        binding?.listview?.layoutManager = LinearLayoutManager(requireContext())
        binding?.listview?.adapter = KotlinDataAdapter.Builder<String>()
            .setLayoutId(android.R.layout.simple_list_item_1)
            .setData(arrayListOf("1","2","3"))
            .addBindView { itemView, itemData ->
                itemView.findViewById<TextView>(android.R.id.text1).setText("@" + itemData)
            }
            .create()


    }
}