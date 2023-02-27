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
import com.example.myapplication.entity.AdminBean
import com.example.myapplication.databinding.ActivityBaseListBinding
import com.example.myapplication.entity.UserBean
import com.example.myapplication.http.AdminUtils
import com.example.myapplication.http.UserUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.ViewUtils
import com.example.myapplication.utils.extensions.setOnItemClickListener
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseListBinding

    private val list = mutableListOf<AdminBean>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setBarsFontLightColor(this, true)
        binding = ActivityBaseListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.baseTitle.text = "主题设置"
        binding.baseBack.setOnClickListener { finish() }

        val user: UserBean? = try {
            Gson().fromJson(Prefs.userInfo, UserBean::class.java)
        } catch (e: Exception) {
            null
        }

        if (user == null) {
            Toast.makeText(this, "未登录，请先登录", Toast.LENGTH_SHORT).show()
            return
        }

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

                icon.setImageResource(R.drawable.ic_admin_style)
                title.text = item.name + "@" + item.contactName
            }
        }
        binding.baseRecyclerView.setOnItemClickListener { holder, position ->
            val adminBean = list[position]

            lifecycleScope.launch(Dispatchers.IO) {
                val flag = try {
                    UserUtils.modifyBelong(user.account!!, adminBean.id.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }

                withContext(Dispatchers.Main) {
                    if (flag) {
                        Prefs.adminInfo = Gson().toJson(adminBean)
                        Toast.makeText(this@AdminActivity, "切换成功", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AdminActivity, "切换失败，请检查网络", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        lifecycleScope.launch(Dispatchers.IO) {
            val subList = try {
                AdminUtils.getAll()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            withContext(Dispatchers.Main) {
                if (subList != null) {
                    list.clear()
                    list.addAll(subList)
                    binding.baseRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }

    }
}