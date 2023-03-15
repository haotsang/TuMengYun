package com.example.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.adapter.KotlinDataAdapter
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.entity.NavItem
import com.example.myapplication.listener.DragTouchListener
import com.example.myapplication.modules.user.activity.GroupActivity
import com.example.myapplication.modules.user.activity.LoginActivity
import com.example.myapplication.modules.user.activity.RegionActivity
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.Utils
import com.example.myapplication.utils.VersionUtils
import com.example.myapplication.utils.ViewUtils
import com.example.myapplication.utils.extensions.setOnItemClickListener
import com.example.myapplication.utils.extensions.toColor
import com.example.myapplication.view.CustomDialog
import com.example.myapplication.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

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
    private var apkPath = ""
    private var doublePressedTime: Long = 0

    private val installApkLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (packageManager.canRequestPackageInstalls()) {
                    val file = File(apkPath)
                    if (apkPath.isNotEmpty() && file.exists()) {
                        VersionUtils.installApkFile(this@MainActivity, file)
                        apkPath = ""
                    }
                } else {
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setBarsFontLightColor(this, status = false, navigation = true)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        val fragmentContainerView =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = fragmentContainerView.navController
//        val navController = findNavController(R.id.nav_host_fragment_container)
//        val appBarConfiguration= AppBarConfiguration(setOf(R.id.menu1_fragment,R.id.menu2_fragment,R.id.menu3_fragment,R.id.menu4_fragment))
//        setupActionBarWithNavController(navController,appBarConfiguration)

        NavigationUI.setupWithNavController(binding.bottom, navController)

//        Navigation.findNavController(this, R.id.nav_host_fragment_container)


        initView()
        initNavRecyclerView()

        checkUpdate(false)
    }

    private fun initView() {
        binding.contentScan.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }
        binding.contentSearch.setOnClickListener {
            startActivity(Intent(this, RegionActivity::class.java))
        }
        binding.contentMenu.setOnClickListener {
            binding.drawerlayout.openDrawer(GravityCompat.END)
        }

        binding.drawerlayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

//                navList.getOrNull(0)?.enable = !Prefs.isLogin
//                navList.getOrNull(1)?.enable = !Prefs.isLogin
                binding.navRecyclerView.adapter?.notifyDataSetChanged()

//                ViewUtils.setBarsFontLightColor(this@MainActivity, status = true, navigation = true)
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)

//                ViewUtils.setBarsFontLightColor(this@MainActivity, status = false, navigation = true)
            }
        })

//        binding.bottom.setOnItemSelectedListener {
//            // 避免再次点击重复创建
//            if (it.isChecked) {
//                return@setOnItemSelectedListener true
//            }
//            // 避免B返回到A重复创建
//            val popBackStack = navController.popBackStack(it.itemId, false)
//            if (popBackStack) {
//                // 已创建
//                return@setOnItemSelectedListener popBackStack
//            } else {
//                // 未创建
//                return@setOnItemSelectedListener NavigationUI.onNavDestinationSelected(
//                    it, navController
//                )
//            }
//        }

        Glide.with(this).load(R.drawable.animal)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)).centerCrop())
            .into(binding.animalView)
        val touchListener = DragTouchListener(binding.animalView)
        touchListener.setDirect(DragTouchListener.FREE)
        binding.animalView.setOnTouchListener(touchListener)
        binding.animalView.setOnClickListener { touchListener.reset() }
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

                title.setTextColor(if (itemData.enable) this.toColor(R.color.black) else Color.GRAY)
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
                    LoginActivity.start(this, "register")
                }
                "main_menu_login" -> {
                    LoginActivity.start(this, "login")
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
                    checkUpdate(true)
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
            LoginActivity.start(this, "admin")
        }
    }

    private fun checkUpdate(toast: Boolean) {
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
                    if (toast) {
                        Toast.makeText(this@MainActivity, "未检测到新版本", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun startUpdate() {
        val dialog = CustomDialog.Builder2(this)
            .setIcon(R.drawable.ic_alert_wait)
            .setCancelable(false)
            .setCustomView(ProgressBar(this))
            .show()
        lifecycleScope.launch(Dispatchers.IO) {
            apkPath = VersionUtils.okDownload(this@MainActivity)

            withContext(Dispatchers.Main) {
                dialog.dismiss()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !packageManager.canRequestPackageInstalls()) {
                    val packageURI = Uri.parse("package:$packageName")
                    val intent =  Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
                    installApkLauncher.launch(intent)
                } else {
                    VersionUtils.installApkFile(this@MainActivity, File(apkPath))
                }
            }
        }
    }

    private fun onBack() {
        if (binding.drawerlayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerlayout.closeDrawer(GravityCompat.END)
        } else {
            if (!navController.navigateUp()) {
                val mNowTime = System.currentTimeMillis()  //记录本次按键时刻
                if (mNowTime - doublePressedTime > 2000) {  //比较两次按键时间差
                    Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show()
                    doublePressedTime = mNowTime
                } else {
                    finish()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, binding.drawerlayout)
    }
}