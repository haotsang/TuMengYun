package com.example.myapplication.modules.area.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.activity.BaseActivity
import com.example.myapplication.adapter.KotlinDataAdapter
import com.example.myapplication.databinding.ActivityBaseListBinding
import com.example.myapplication.http.ExhibitUtils
import com.example.myapplication.modules.area.entity.ExhibitBean
import com.example.myapplication.utils.ViewUtils
import com.example.myapplication.utils.extensions.setOnItemClickListener
import com.example.myapplication.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExhibitListActivity : BaseActivity() {

    private lateinit var binding: ActivityBaseListBinding

    private val list = mutableListOf<ExhibitBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setBarsFontLightColor(this, status = true, navigation = true)
        binding = ActivityBaseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.baseTitle.text = "场馆设置"
        binding.baseBack.setOnClickListener { finish() }

        binding.baseOverflow.text = "添加展品"
        binding.baseOverflow.setOnClickListener {
            startActivity(Intent(this, AddExhibitActivity::class.java))
        }

        val adapter = KotlinDataAdapter.Builder<ExhibitBean>()
            .setLayoutId(R.layout.item_location_info)
            .setData(list)
            .addBindView { itemView, itemData ->
                val icon = itemView.findViewById<ImageView>(R.id.item_info_icon)
                val title = itemView.findViewById<TextView>(R.id.item_info_title)
                val arrow = itemView.findViewById<ImageView>(R.id.item_info_arrow)

                icon.visibility = View.GONE
                title.text = itemData.name
            }.create()

        binding.baseRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.baseRecyclerView.adapter = adapter
        binding.baseRecyclerView.setOnItemClickListener { holder, position ->
            list[position].id
//            getDesc
            startActivity(Intent(this@ExhibitListActivity, AddExhibitActivity::class.java))
        }


        lifecycleScope.launch(Dispatchers.IO) {
            val l2 = try {
                ExhibitUtils.getSimpleExhibitsByPin(UserViewModel.region?.pin!!)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }

            withContext(Dispatchers.Main) {
                list.clear()
                if (l2 != null) {
                    list.addAll(l2)
                }
                adapter.notifyDataSetChanged()
            }

        }

    }
}