package com.example.myapplication.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.myapplication.R
import com.example.myapplication.adapter.BannerImageAdapter2
import com.example.myapplication.bean.BannerItem
import com.example.myapplication.databinding.ActivityMainBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            try {
//                LoginUtils.login2("1234","1234")

//                JDBCUtils.test()
//
//                Thread{
//                    val a = HttpUtils.test1()
//                    runOnUiThread {
//                        MaterialAlertDialogBuilder(this).setMessage(a).show()
//                    }
//                }.start()


            } catch (e: Exception) {
                MaterialAlertDialogBuilder(this).setMessage(e.stackTraceToString()).show()
            }



            binding.drawerlayout.openDrawer(GravityCompat.END)
        }
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
                    startActivity(Intent(this, ManagerActivity::class.java))
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


    }
}