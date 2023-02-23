package com.example.myapplication.bean

data class NavItem(var enable: Boolean, val id: String, val title: String, val icon: Int, val top: Boolean = false, val bottom: Boolean = false)
