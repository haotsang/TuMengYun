package com.example.myapplication.activity.user

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.adapter.KotlinDataAdapter
import com.example.myapplication.databinding.ActivityBaseListBinding
import com.example.myapplication.entity.RegionBean
import com.example.myapplication.utils.ViewUtils
import com.example.myapplication.utils.extensions.setOnItemClickListener
import com.example.myapplication.utils.livebus.LiveDataBus
import com.example.myapplication.viewmodel.UserViewModel

class RegionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseListBinding

    private val list = mutableListOf<RegionBean>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setBarsFontLightColor(this, status = true, navigation = true)
        binding = ActivityBaseListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.baseTitle.text = "主题列表"
        binding.baseBack.setOnClickListener { finish() }

        if (UserViewModel.user == null) {
            Toast.makeText(this, "未登录，请先登录", Toast.LENGTH_SHORT).show()
            return
        }

        LiveDataBus.with("getAllRegion").observe(this) {
            val subList = it as List<RegionBean>?
            if (subList != null) {
                list.clear()
                list.addAll(subList)
                binding.baseRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
        UserViewModel.getAllRegion(lifecycleScope)

        LiveDataBus.with("livebus_modify_pin").observe(this) {
            val pair = it as Pair<Boolean, String>
            if (pair.first) {
                Toast.makeText(this@RegionActivity, "切换成功", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@RegionActivity, "切换失败，${pair.second}", Toast.LENGTH_SHORT).show()
            }
        }


        val adapter = KotlinDataAdapter.Builder<RegionBean>()
            .setLayoutId(R.layout.item_nav)
            .setData(list)
            .addBindView { itemView, itemData ->
                val icon = itemView.findViewById<ImageView>(R.id.item_nav_icon)
                val title = itemView.findViewById<TextView>(R.id.item_nav_title)
                val arrow = itemView.findViewById<ImageView>(R.id.item_nav_arrow)

                icon.setImageResource(R.drawable.ic_admin_style)
                title.text = itemData.name + "@" + itemData.contactName
            }.create()

        binding.baseRecyclerView.adapter = adapter
        /*
        binding.baseRecyclerView.setOnItemClickListener { holder, position ->
            val adminBean = list[position]
            UserViewModel.modifyPin(
                lifecycleScope,
                adminBean,
                UserViewModel.user?.account!!,
                adminBean.pin.toString()
            )
        }
        */
    }
}