package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bean.UserBean
import com.example.myapplication.databinding.ActivityGroupBinding
import com.example.myapplication.utils.LoginUtils
import com.example.myapplication.utils.extensions.setOnItemClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StaffActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupBinding

    private val list = mutableListOf<UserBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGroupBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.toolbar.title = "工作人员"
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_cangku, parent, false)
                return object : RecyclerView.ViewHolder(view){}
            }
            override fun getItemCount(): Int = list.size
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = list[holder.adapterPosition]
                val tv = holder.itemView.findViewById<TextView>(R.id.item_name)
                val role = when (item.role) {
                    0 -> "普通用户"
                    1 -> "工作人员"
                    2 -> "管理员"
                    else -> null
                }
                tv.setText(item.nickname + "-" + role + "-" + (item.belong ?: "未知社区/科技馆"))

            }
        }
        binding.recyclerView.setOnItemClickListener { holder, position ->
            val item = list[position]
            val role = if (item.role == 0) {
                "工作人员"
            } else {
                "普通用户"
            }

            MaterialAlertDialogBuilder(this).setMessage("是否将此用户设置为${role}？")
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    lifecycleScope.launch(Dispatchers.IO) {

                        val state = if (item.role == 1) {
                            LoginUtils.modifyRole(item.account!!, "0")
                        } else {
                            LoginUtils.modifyRole(item.account!!, "1")
                        }

                        withContext(Dispatchers.Main) {
                            if (state) {
                                Toast.makeText(this@StaffActivity, "设置成功！", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@StaffActivity, "失败", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }.show()
        }


        lifecycleScope.launch(Dispatchers.IO) {
            val json = LoginUtils.getAllUsers()
            withContext(Dispatchers.Main) {
                list.clear()
                list.addAll(json)
                binding.recyclerView.adapter?.notifyDataSetChanged()
            }
        }

    }
}