package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.adapter.BannerImageAdapter2
import com.example.myapplication.bean.BannerItem
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils.LabelImgUtils
import com.example.myapplication.utils.LabelUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.Utils
import com.google.gson.JsonObject
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val labelImgList = mutableListOf<BannerItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.content.contentMenu.setOnClickListener {
            binding.drawerlayout.openDrawer(GravityCompat.END)
        }
        binding.drawerlayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                binding.navView.menu.findItem(R.id.main_menu_register).isEnabled = !Prefs.isLogin
                binding.navView.menu.findItem(R.id.main_menu_login).isEnabled = !Prefs.isLogin
            }
        })
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.main_menu_register -> {
                    startActivity(Intent(this, RegisterActivity::class.java).apply {
                        putExtra("register", true)
                    })
                }
                R.id.main_menu_login -> {
                    startActivity(Intent(this, RegisterActivity::class.java).apply {
                        putExtra("register", false)
                    })
                }
                R.id.main_menu_manager -> {
                    startManagerPage()
                }
                R.id.main_menu_clean_cache -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        Utils.deleteDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES))
//                        Glide.get(this@MainActivity).clearMemory()
//                        Glide.get(this@MainActivity).clearDiskCache()
//                        try {
//                            Thread.sleep(1000)
//                        } catch (e: InterruptedException) {
//                            e.printStackTrace()
//                        }

                        withContext(Dispatchers.Main) {
//                            binding.drawerlayout.close()
                            Toast.makeText(this@MainActivity, "清理完成！", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                R.id.main_menu_repoter -> {
                    startActivity(Intent(this, IssueActivity::class.java))
                }
                R.id.main_menu_group -> {
                    startActivity(Intent(this, GroupActivity::class.java))
                }
                R.id.main_menu_tuisong -> {

                }
            }
            true
        }


        binding.content.banner.setAdapter(BannerImageAdapter2(labelImgList, this))
            .addBannerLifecycleObserver(this)
            .setIndicator(CircleIndicator(this))
            .setLoopTime(1500)
            .setOnBannerListener { data, position ->

            }



        binding.content.bottomScan.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }
        binding.content.bottomMine.setOnClickListener {
            startActivity(Intent(this, MineActivity::class.java))
        }

    }

    private fun startManagerPage() {
        if (!Prefs.isLogin) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show()
            return
        }
        if (Prefs.isAdmin) {
            startActivity(Intent(this, ManagerActivity::class.java))
        } else {
            Toast.makeText(this, "请先申请成为管理员", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, RegisterManagerActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        try {
            binding.content.contentTitle.text = JSONObject(Prefs.curRegionId).optString("name")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val labelBean = try {
                LabelUtils.getLabel(JSONObject(Prefs.curRegionId).optString("id"))
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }

            val img = if (labelBean != null && labelBean.visible == 1) {
                LabelImgUtils.getLabelImgList(JSONObject(Prefs.curRegionId).optString("id"))
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
                            this.id = it.id.toInt()
                            this.imagePath = it.uri
                            this.order = it.tid.toInt()
                        }
                    })
                }
                binding.content.banner.adapter.notifyDataSetChanged()

            }
        }
    }

    override fun onDestroy() {
        if (!Prefs.isSaveStatus) {
            Prefs.isLogin = false
            Prefs.isAdmin = false
            Prefs.userAccount = ""
            Prefs.userPassword = ""
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (binding.drawerlayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerlayout.close()
        } else {
            super.onBackPressed()
        }
    }

}