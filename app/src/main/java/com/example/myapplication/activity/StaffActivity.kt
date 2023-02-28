package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityBaseListBinding
import com.example.myapplication.entity.UserBean
import com.example.myapplication.http.UserUtils
import com.example.myapplication.utils.ViewUtils
import com.example.myapplication.utils.extensions.setOnItemClickListener
import com.example.myapplication.view.CustomDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StaffActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseListBinding

    private val list = mutableListOf<UserBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setBarsFontLightColor(this, true)
        binding = ActivityBaseListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.baseTitle.text = "工作人员"
        binding.baseBack.setOnClickListener { finish() }

        binding.baseRecyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_nav, parent, false)
                return object : RecyclerView.ViewHolder(view){}
            }
            override fun getItemCount(): Int = list.size
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = list[holder.adapterPosition]
                val icon = holder.itemView.findViewById<ImageView>(R.id.item_nav_icon)
                val title = holder.itemView.findViewById<TextView>(R.id.item_nav_title)
                val arrow = holder.itemView.findViewById<ImageView>(R.id.item_nav_arrow)

                val role = when (item.role) {
                    0 -> "普通用户"
                    1 -> "工作人员"
                    2 -> "管理员"
                    else -> null
                }
                icon.setImageResource(R.drawable.ic_admin_staff)
                title.text = "${item.nickname}@${role}@${(item.belong ?: "未知社区/科技馆")}"

            }
        }
        binding.baseRecyclerView.setOnItemClickListener { holder, position ->
            val item = list[position]
            if (item.role == 2) {
                CustomDialog.Builder2(this)
                    .setIcon(R.drawable.ic_alert_error)
                    .setTitle("不可对管理员进行操作")
                    .setConfirmText(android.R.string.ok)
                    .setConfirmListener { }
                    .show()
                return@setOnItemClickListener
            }

            val role = if (item.role == 0) {
                "工作人员"
            } else {
                "普通用户"
            }

            CustomDialog.Builder2(this)
                .setIcon(R.drawable.ic_alert_ask)
                .setTitle("是否将此用户设置为${role}？")
                .setCancelText(android.R.string.cancel)
                .setConfirmText(android.R.string.ok)
                .setCancelListener { }
                .setConfirmListener {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val state = when (item.role) {
                            1 -> UserUtils.modifyRole(item.account!!, "0")
                            0 -> UserUtils.modifyRole(item.account!!, "1")
                            else -> false
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
            val subList = try {
                UserUtils.getAllUsers()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }
            withContext(Dispatchers.Main) {
                list.clear()
                if (subList != null) {
                    list.addAll(subList)
                }
                binding.baseRecyclerView.adapter?.notifyDataSetChanged()
            }
        }

    }
}