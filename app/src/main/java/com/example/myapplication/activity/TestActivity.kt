package com.example.myapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val fragmentContainerView=supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val bottom=findViewById<BottomNavigationView>(R.id.bottom)
        val navController=fragmentContainerView.navController
//        val appBarConfiguration= AppBarConfiguration(setOf(R.layout.fragment_two,R.layout.fragment_one,R.layout.fragment_three,R.layout.fragment_four))
//        setupActionBarWithNavController(navController,appBarConfiguration)
        bottom.setupWithNavController(navController)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return NavigationUI.navigateUp(binding.drawerLayout, navController)
//    }
//
//    override fun onBackPressed() {
//        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            binding.drawerLayout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }
}