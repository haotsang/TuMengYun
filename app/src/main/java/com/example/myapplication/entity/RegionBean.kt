package com.example.myapplication.entity

data class RegionBean(
    var id: Int? = null,
    var name: String? = null,
    var account: String? = null,
    var password: String? = null,
    var contactName: String? = null,
    var contactPhone: String? = null,
    var address: String? = null,
    var enable: Int = 0,
    var pin: String? = null,
)