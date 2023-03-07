package com.example.myapplication.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.activity.label.QuestionActivity
import com.example.myapplication.activity.user.*
import com.example.myapplication.adapter.BannerImageAdapter2
import com.example.myapplication.adapter.KotlinDataAdapter
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.entity.*
import com.example.myapplication.http.*
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.Utils
import com.example.myapplication.utils.VersionUtils
import com.example.myapplication.utils.extensions.setOnItemClickListener
import com.example.myapplication.utils.extensions.toColor
import com.example.myapplication.utils.livebus.LiveDataBus
import com.example.myapplication.view.CustomDialog
import com.example.myapplication.viewmodel.LabelViewModel
import com.example.myapplication.viewmodel.MainViewModel
import com.example.myapplication.viewmodel.UserViewModel
import com.google.gson.Gson
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

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
        NavItem(true, "main_menu_check_update", "检查更新", 0),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initView()
        initMainCard()
        initNavRecyclerView()

        LiveDataBus.with("livebus_user_change").observe(this) {
            LabelViewModel.getLabel(lifecycleScope, UserViewModel.user?.pin.toString())
            LabelViewModel.getLabelImg(lifecycleScope, LabelViewModel.label?.id.toString())

            val user = it as UserBean?
            println("#####  livebus_user_change")
        }
        LiveDataBus.with("livebus_region_change").observe(this) {
            LabelViewModel.getLabel(lifecycleScope, UserViewModel.user?.pin.toString())
            LabelViewModel.getLabelImg(lifecycleScope, LabelViewModel.label?.id.toString())

            val region = it as RegionBean?

            binding.content.contentTitle.text = region?.name ?: ""
            println("#####  livebus_region_change")
        }

        LiveDataBus.with("livebus_label_change").observe(this) {
            val labelBean = it as LabelBean?
            if (labelBean != null) {
                binding.content.bannerText.text = if (labelBean.visible == 1) {
                    (labelBean.title ?: "未设置") + "\n" + (labelBean.content ?: "未设置")
                } else "未设置1"
            } else {
                binding.content.bannerText.text = "empty"
            }

            println("#####  livebus_label_change")
        }
        LiveDataBus.with("livebus_label_img_change").observe(this) {
            val imgList = it as List<LabelImgBean>?

            val img = if (LabelViewModel.label != null && LabelViewModel.label?.visible == 1) {
                imgList
            } else null

            labelImgList.clear()
            if (img != null) {
                labelImgList.addAll(img.map {
                    BannerItem().apply {
                        this.id = it.id
                        this.imagePath = it.uri
                        this.lid = it.lid
                    }
                })
            }
            binding.content.banner.setDatas(labelImgList)

            println("#####  livebus_label_img_change")
        }

        LabelViewModel.getLabel(lifecycleScope, UserViewModel.user?.pin.toString())
        LabelViewModel.getLabelImg(lifecycleScope, LabelViewModel.label?.id.toString())

    }

    private fun initView() {

        binding.content.contentTitle.setOnClickListener {
            startActivity(Intent(this, RegionActivity::class.java))
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
            .setOnBannerListener { data, position -> }

        binding.content.bannerText.setOnClickListener {
            if (UserViewModel.user != null && LabelViewModel.label != null) {
                startActivity(Intent(this, QuestionActivity::class.java).apply {
                    putExtra("label_id", LabelViewModel.label?.id)
                })
            }
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


    }


    private fun initNewLabel() {
        val admin: RegionBean? = try {
            Gson().fromJson("Prefs.adminInfo", RegionBean::class.java)
        } catch (e: Exception) {
            null
        }

        binding.content.contentTitle.text = admin?.name ?: ""

        if (admin == null) return

        lifecycleScope.launch(Dispatchers.IO) {
            val labelBean = try {
                LabelUtils.getLabelByPin(admin.id.toString())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }

            val img = if (labelBean != null && labelBean.visible == 1) {
                try {
                    LabelImgUtils.getLabelImgList(labelBean.id.toString())
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
                            this.lid = it.lid
                        }
                    })
                }
                binding.content.banner.adapter.notifyDataSetChanged()

            }
        }
    }

    private fun initNewInfo() {
        val user: UserBean = try {
            Gson().fromJson(Prefs.userInfo, UserBean::class.java)
        } catch (e: Exception) {
            null
        } ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            val responseBase: ResponseBase? = try {
                if (Prefs.isLoginFromPhone) {
                    UserUtils.loginWithPhone(user.phone!!, user.pin.toString())
                } else {
                    UserUtils.login(user.account!!, user.password!!)
                }
            } catch (e: Exception) {
                null
            }

            val admin = try {
                RegionUtils.getRegionById(user.pin.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            withContext(Dispatchers.Main) {
                if (responseBase != null && responseBase.code == 200) {
                    Prefs.userInfo = Gson().toJson(responseBase.data)
                } else {
                    Prefs.userInfo = ""
                }

                if (admin != null) {
//                    Prefs.adminInfo = Gson().toJson(admin)
                } else {
//                    Prefs.adminInfo = ""
                }

//                try {
//                    binding.content.contentTitle.text =
//                        Gson().fromJson(Prefs.adminInfo, AdminBean::class.java)?.name ?:""
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
            }
        }

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
        val adapter = KotlinDataAdapter.Builder<NavItem>()
            .setLayoutId(R.layout.item_nav)
            .setData(navList)
            .addBindView { itemView, itemData ->
                val icon = itemView.findViewById<ImageView>(R.id.item_nav_icon)
                val title = itemView.findViewById<TextView>(R.id.item_nav_title)
                val arrow = itemView.findViewById<ImageView>(R.id.item_nav_arrow)
                icon.setImageResource(itemData.icon)
                title.text = itemData.title
                arrow.setImageResource(
                    if (itemData.id == "main_menu_tuisong")
                        if (Prefs.tuiSong) R.drawable.ic_switch_on else R.drawable.ic_switch_off
                    else R.drawable.ic_nav_right
                )

                title.setTextColor(if (itemData.enable) this@MainActivity.toColor(R.color.black) else Color.GRAY)
                itemView.isEnabled = itemData.enable
            }.create()

        binding.navRecyclerView.adapter = adapter
        binding.navRecyclerView.setOnItemClickListener { holder, position ->
            val item = navList[position]
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
                    adapter.notifyItemChanged(position)
//                            throw java.lang.NullPointerException("Test")
                }
                "main_menu_check_update" -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val version = try {
                            VersionUtils.getNewVersion()
                        } catch (e: Exception) {
                            "-1"
                        }

                        withContext(Dispatchers.Main) {
                            val cur = VersionUtils.getVersionName(this@MainActivity)
                            if (version != "-1" && cur != version) {
                                CustomDialog.Builder2(this@MainActivity)
                                    .setIcon(R.drawable.ic_alert_ask)
                                    .setTitle("检测到新版本")
                                    .setCancelListener {  }
                                    .setConfirmListener { startUpdate() }
                                    .show()
                            } else {
                                Toast.makeText(this@MainActivity, "未检测到新版本", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
    private fun startManagerPage() {
        val user = UserViewModel.user
        if (user == null) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show()
            return
        }

        if (user.role == 2) {
            startActivity(Intent(this, ManagerActivity::class.java))
        } else {
            Toast.makeText(this, "请先申请成为管理员", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, RegisterManagerActivity::class.java))
        }
    }

    private fun startUpdate() {
        val dialog = CustomDialog.Builder2(this)
            .setIcon(R.drawable.ic_alert_wait)
            .setCancelable(false)
            .setCustomView(ProgressBar(this))
            .show()
        lifecycleScope.launch(Dispatchers.IO) {
            val apkPath = VersionUtils.okDownload(this@MainActivity)

            withContext(Dispatchers.Main) {
                dialog.dismiss()
                VersionUtils.install(this@MainActivity, File(apkPath))
            }
        }
    }

    override fun onDestroy() {
        if (!Prefs.isSaveStatus) {
            Prefs.userInfo = ""
        }

        val user = UserViewModel.user
        if (user != null) {
            val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "${user.id}.questions")
            if (file.exists()) file.delete()
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