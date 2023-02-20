package com.example.myapplication.bean

data class BannerItem(
    val desc: String?=null,
    var id: Int?=null,
    var imagePath: String?=null,
    val isVisible: Int?=null,
    var order: Int?=null,
    val title: String?=null,
    val type: Int?=null,
    val url: String?=null
)
