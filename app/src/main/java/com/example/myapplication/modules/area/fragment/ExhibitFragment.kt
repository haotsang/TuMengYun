package com.example.myapplication.modules.area.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentInfoBinding
import com.example.myapplication.fragment.ImageViewerDialogFragment
import com.example.myapplication.http.ExhibitUtils
import com.example.myapplication.modules.area.activity.VrActivity
import com.example.myapplication.modules.area.adapter.InfoBannerAdapter
import com.example.myapplication.modules.area.adapter.InfoListAdapter
import com.example.myapplication.modules.area.entity.ExhibitDesc
import com.example.myapplication.utils.JDBCUtils
import com.example.myapplication.utils.extensions.setOnItemClickListener
import com.example.myapplication.viewmodel.UserViewModel
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExhibitFragment : Fragment() {

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
        binding?.baseTitle?.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                JDBCUtils.test()
            }

        }

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
                val url = (data as ExhibitDesc).url.toString()
                ImageViewerDialogFragment.show(this, url)
            }


        lifecycleScope.launch(Dispatchers.IO) {
            val sub = try {
                ExhibitUtils.getSimpleExhibitsByPin(UserViewModel.region?.pin!!)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }

            withContext(Dispatchers.Main) {
                if (sub != null) {
                    adapter.setDataList(sub)
                }

                check(adapter, 0)
            }
        }


    }

    private fun check(adapter: InfoListAdapter, position: Int) {

        lifecycleScope.launch(Dispatchers.IO) {
            val sub = try {
                ExhibitUtils.getDesc(adapter.list.get(position).id.toString())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }

            withContext(Dispatchers.Main) {
                if (sub != null) {
                    adapter.selection = position
                    binding?.banner!!.setDatas(sub)
                }
            }
        }


    }
}