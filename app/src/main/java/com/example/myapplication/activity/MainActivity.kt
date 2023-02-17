package com.example.myapplication.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myapplication.R
import com.example.myapplication.adapter.BannerImageAdapter2
import com.example.myapplication.bean.BannerItem
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils.Prefs
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.youth.banner.indicator.CircleIndicator


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)



//得到Legend对象
        //得到Legend对象
        val legend: Legend = binding.content.chart1.getLegend()
        //设置字体大小
        legend.setTextSize(18f)
        //设置排列方式
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        //设置图例的大小
        legend.setFormSize(15f)


        //得到Description对象
        val description: Description = binding.content.chart1.getDescription()
        //设置文字
        description.setText("这是柱状图的标题")
        //设置文字大小
        description.setTextSize(18f)
        //设置偏移量

        // 获取屏幕中间x 轴的像素坐标
        //设置偏移量

        // 获取屏幕中间x 轴的像素坐标
        val x = (com.example.myapplication.utils.ViewUtils.getWindowWidth(this) / 2).toFloat()

        description.setPosition(x, 50f)
        //设置字体颜色
        description.setTextColor(Color.BLUE)
        //设置字体加粗
        description.setTypeface(Typeface.DEFAULT_BOLD)


//        binding.content.chart1.setExtraTopOffset(25f);
//        binding.content.chart1.setExtraLeftOffset(30f);
//        binding.content.chart1.setExtraRightOffset(100f);
//        binding.content.chart1.setExtraBottomOffset(50f);

        val barEntries: MutableList<BarEntry> = ArrayList()
        barEntries.add(BarEntry(0f, 5f))
        barEntries.add(BarEntry(1f, 10f))
        barEntries.add(BarEntry(2f, 13f))

        val barDataSet = BarDataSet(barEntries, "图例标题")
        val ba = BarData(barDataSet)
        binding.content.chart1.setData(ba)


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

                }
                R.id.main_menu_repoter -> {
                    startActivity(Intent(this, IssueActivity::class.java))
                }
                R.id.main_menu_group -> {
                    startActivity(Intent(this, TuMengGroupActivity::class.java))
                }
                R.id.main_menu_tuisong -> {

                }
            }
            true
        }


        val imgList = mutableListOf<BannerItem>()

        imgList.add(BannerItem(imagePath = "https://profile.csdnimg.cn/1/7/8/3_weixin_45882303", title = "11111111111111"))
        imgList.add(BannerItem(imagePath = "https://gw.alicdn.com/bao/uploaded/i3/2210144838464/O1CN01N6ijB52COZ6y9neU6_!!0-item_pic.jpg_210x210q75.jpg_.webp", title = "2222222222222"))
        imgList.add(BannerItem(imagePath = "https://gw.alicdn.com/imgextra/i2/O1CN01nlPQ9s1y3aKUb5tDo_!!6000000006523-0-tps-800-450.jpg", title = "3333333333333"))

        binding.content.banner.setAdapter(BannerImageAdapter2(imgList, this))
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
        if (Prefs.isManager) {
            startActivity(Intent(this, ManagerActivity::class.java))
        } else {
            Toast.makeText(this, "请先申请成为管理员", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, RegisterManagerActivity::class.java))
        }

    }

    override fun onDestroy() {
        if (!Prefs.isSaveStatus) {
            Prefs.isLogin = false
            Prefs.isManager = false
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