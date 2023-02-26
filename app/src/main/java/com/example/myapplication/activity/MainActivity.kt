package com.example.myapplication.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.BannerImageAdapter2
import com.example.myapplication.entity.BannerItem
import com.example.myapplication.entity.NavItem
import com.example.myapplication.entity.UserBean
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.entity.AdminBean
import com.example.myapplication.http.AdminUtils
import com.example.myapplication.http.LabelImgUtils
import com.example.myapplication.http.LabelUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.Utils
import com.example.myapplication.utils.extensions.toColor
import com.example.myapplication.view.CustomDecoration
import com.google.gson.Gson
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val labelImgList = mutableListOf<BannerItem>()


    private val navList = mutableListOf(
        NavItem(true, "main_menu_register", "注册账号", R.drawable.ic_nav_register),
        NavItem(true, "main_menu_login", "登录", R.drawable.ic_nav_login),
        NavItem(true, "main_menu_admin", "智慧管理", R.drawable.ic_nav_admin),
        NavItem(true, "main_menu_clean_cache", "清理缓存", R.drawable.ic_nav_clear),
        NavItem(true, "main_menu_issue", "意见反馈", R.drawable.ic_nav_issue),
        NavItem(true, "main_menu_group", "突梦群", R.drawable.ic_nav_group),
        NavItem(true, "main_menu_tuisong", "接受推送", R.drawable.ic_nav_tuisong),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.content.contentTitle.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }
        binding.content.contentMenu.setOnClickListener {
            binding.drawerlayout.openDrawer(GravityCompat.END)
        }
        binding.drawerlayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

//                navList.getOrNull(0)?.enable = !Prefs.isLogin
//                navList.getOrNull(1)?.enable = !Prefs.isLogin
                binding.navRecyclerView.adapter?.notifyDataSetChanged()

            }
        })


        binding.content.banner.setAdapter(BannerImageAdapter2(labelImgList, this))
            .addBannerLifecycleObserver(this)
            .setIndicator(CircleIndicator(this))
            .setLoopTime(1500)
            .setOnBannerListener { data, position ->

            }



        binding.content.bottomScan.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }
        binding.content.bottomSpace.setOnClickListener {

        }
        binding.content.bottomShop.setOnClickListener {

        }
        binding.content.bottomMine.setOnClickListener {
            startActivity(Intent(this, MineActivity::class.java))
        }


        initMainCard()
        initNavRecyclerView()
    }

    private fun initMainCard() {
        binding.content.cardItem1.cardImg.setImageResource(R.drawable.ic_card_flow)
        binding.content.cardItem1.cardTips.text = "客流"
        binding.content.cardItem1.cardValue.text = "200人"

        binding.content.cardItem2.cardImg.setImageResource(R.drawable.ic_card_power)
        binding.content.cardItem2.cardTips.text = "能源消耗"
        binding.content.cardItem2.cardValue.text = "20°电"

        binding.content.cardItem3.cardImg.setImageResource(R.drawable.ic_card_damage)
        binding.content.cardItem3.cardTips.text = "设备损坏"
        binding.content.cardItem3.cardValue.text = "2件"

        binding.content.cardItem4.cardImg.setImageResource(R.drawable.ic_card_staff)
        binding.content.cardItem4.cardTips.text = "工作人员"
        binding.content.cardItem4.cardValue.text = "30人"
    }

    private fun initNavRecyclerView() {
        binding.navRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.navRecyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int,
            ): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_nav, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun getItemCount(): Int = navList.size
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = navList[holder.adapterPosition]

                val icon =
                    holder.itemView.findViewById<ImageView>(R.id.item_nav_icon)
                val title =
                    holder.itemView.findViewById<TextView>(R.id.item_nav_title)
                val arrow =
                    holder.itemView.findViewById<ImageView>(R.id.item_nav_arrow)
                icon.setImageResource(item.icon)
                title.text = item.title
                arrow.setImageResource(
                    if (item.id == "main_menu_tuisong")
                        if (Prefs.tuiSong) R.drawable.ic_switch_on else R.drawable.ic_switch_off
                    else R.drawable.ic_nav_right
                )

                title.setTextColor(if (item.enable) this@MainActivity.toColor(R.color.black) else Color.GRAY)
                holder.itemView.isEnabled = item.enable
                holder.itemView.setOnClickListener {
                    if (item.id != "main_menu_tuisong") {
                        binding.drawerlayout.closeDrawer(GravityCompat.END)
                    }

                    when (item.id) {
                        "main_menu_register" -> {
                            startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
                        }
                        "main_menu_login" -> {
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        }
                        "main_menu_admin" -> startManagerPage()
                        "main_menu_clean_cache" -> {
                            lifecycleScope.launch(Dispatchers.IO) {
                                Utils.deleteDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES))

//                        Glide.clear()
//                        video.clear()
//                        cache.delete

                                withContext(Dispatchers.Main) {
                                    Toast.makeText(this@MainActivity, "清理完成！", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                        "main_menu_issue" -> {
                            startActivity(Intent(this@MainActivity, IssueActivity::class.java))
                        }
                        "main_menu_group" -> {
                            startActivity(Intent(this@MainActivity, GroupActivity::class.java))
                        }
                        "main_menu_tuisong" -> {
                            Prefs.tuiSong = !Prefs.tuiSong
                            notifyItemChanged(position)
//                            throw java.lang.NullPointerException("Test")
                        }
                    }
                }
            }
        }
        val itemDecoration = CustomDecoration(
            this, (binding.navRecyclerView.layoutManager as LinearLayoutManager)
                .orientation, false
        )
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.color.ripper)!!)
        binding.navRecyclerView.addItemDecoration(itemDecoration)
    }
    private fun startManagerPage() {
        val user: UserBean? = try {
            Gson().fromJson(Prefs.userInfo, UserBean::class.java)
        } catch (e: Exception) {
            null
        }

//        if (user == null) {
//            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show()
//            return
//        }

//        if (user.role == 2) {
            startActivity(Intent(this, ManagerActivity::class.java))
//        } else {
//            Toast.makeText(this, "请先申请成为管理员", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, RegisterManagerActivity::class.java))
//        }

    }

    override fun onResume() {
        super.onResume()
        val user: UserBean? = try {
            Gson().fromJson(Prefs.userInfo, UserBean::class.java)
        } catch (e: Exception) {
            null
        }
        val curRegionId = if (user?.belong == null) {
            "-1"  //全系统
        } else {
            user.belong.toString()
        }

        binding.content.contentTitle.text = "<全系统>"
        lifecycleScope.launch(Dispatchers.IO) {
            val admin = try {
                AdminUtils.getAdminById(curRegionId)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            withContext(Dispatchers.Main) {
                if (admin != null) {
                    binding.content.contentTitle.text = admin.name

                    Prefs.adminInfo = admin.toString()
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val labelBean = try {
                LabelUtils.getLabel(curRegionId)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }

            val img = if (labelBean != null && labelBean.visible == 1) {
                try {
                    LabelImgUtils.getLabelImgList(curRegionId)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    null
                }
            } else null

            withContext(Dispatchers.Main) {
                if (labelBean != null) {
                    binding.content.bannerText.text = if (labelBean.visible == 1) {
                        (labelBean.title ?: "未设置") + "\n" + (labelBean.content ?: "未设置")
                    } else "未设置1"
                }

                labelImgList.clear()
                if (img != null) {
                    labelImgList.addAll(img.map {
                        BannerItem().apply {
                            this.id = it.id
                            this.imagePath = it.uri
                            this.order = it.tid
                        }
                    })
                }
                binding.content.banner.adapter.notifyDataSetChanged()

            }
        }
    }


    override fun onDestroy() {
        if (!Prefs.isSaveStatus) {
            Prefs.userInfo = ""
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (binding.drawerlayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerlayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

}