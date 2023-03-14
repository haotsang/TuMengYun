package com.example.myapplication.modules.area.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentInfoBinding
import com.example.myapplication.entity.BannerSimpleItem
import com.example.myapplication.fragment.ImageViewerDialogFragment
import com.example.myapplication.modules.area.activity.VrActivity
import com.example.myapplication.modules.area.adapter.InfoBannerAdapter
import com.example.myapplication.modules.area.adapter.InfoListAdapter
import com.example.myapplication.utils.extensions.setOnItemClickListener
import com.youth.banner.indicator.CircleIndicator

class AreaFragment : Fragment() {

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

        initView()
    }

    private fun initView() {
        binding?.baseBack?.setImageResource(0)
        binding?.baseTitle?.text = "梦暴科技馆"


        binding?.baseOverflow?.text = "VR全景"
        binding?.baseOverflow?.setOnClickListener {
            startActivity(Intent(requireContext(), VrActivity::class.java))
        }

        val adapter = InfoListAdapter(requireContext())
        binding?.listview?.layoutManager = LinearLayoutManager(requireContext())
        binding?.listview?.adapter = adapter
        binding?.listview?.setOnItemClickListener { holder, position ->
            check(adapter, position)
        }

        binding?.banner!!.setAdapter(InfoBannerAdapter(arrayListOf(), requireContext()))
            .addBannerLifecycleObserver(this)
            .setIndicator(CircleIndicator(requireContext()))
            .setOnBannerListener { data, position ->
                val url = (data as BannerSimpleItem).url.toString()
                ImageViewerDialogFragment.show(this, url)
            }


        check(adapter, 0)
    }

    private fun check(adapter: InfoListAdapter, position: Int) {
        adapter.selection = position
        binding?.banner!!.setDatas(adapter.list.getOrNull(position)?.bannerList)
    }
}