package com.example.myapplication.modules.user.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.activity.BaseActivity
import com.example.myapplication.adapter.KotlinDataAdapter
import com.example.myapplication.databinding.ActivityBaseListBinding
import com.example.myapplication.entity.UserBean
import com.example.myapplication.utils.ViewUtils
import com.example.myapplication.utils.livebus.LiveDataBus
import com.example.myapplication.viewmodel.UserViewModel

class GroupActivity : BaseActivity() {

    private lateinit var binding: ActivityBaseListBinding

    private val list = mutableListOf<UserBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setBarsFontLightColor(this, status = true, navigation = true)
        binding = ActivityBaseListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.baseTitle.text = "突梦群（0）"
        binding.baseBack.setOnClickListener { finish() }

        val adapter = KotlinDataAdapter.Builder<UserBean>()
            .setLayoutId(R.layout.item_nav)
            .setData(list)
            .addBindView { itemView, itemData ->
                val icon = itemView.findViewById<ImageView>(R.id.item_nav_icon)
                val title = itemView.findViewById<TextView>(R.id.item_nav_title)
                val arrow = itemView.findViewById<ImageView>(R.id.item_nav_arrow)

                icon.setImageResource(R.drawable.ic_nav_group)
                title.text = "${itemData.account}@${itemData.nickname}"
            }.create()
        binding.baseRecyclerView.adapter = adapter

        LiveDataBus.with("getAllUsers").observe(this) {
            val subList = it as List<UserBean>?
            list.clear()
            if (subList != null) {
                list.addAll(subList)
            }
            binding.baseRecyclerView.adapter?.notifyDataSetChanged()

            binding.baseTitle.text = "突梦群（${list.size}）"
        }
        UserViewModel.getAllUsers(lifecycleScope)



    }
}