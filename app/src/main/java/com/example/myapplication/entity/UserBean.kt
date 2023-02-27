package com.example.myapplication.entity

import java.util.*

data class UserBean(
    var id: Int? = null,
    var nickname: String? = null,
    var account: String? = null,
    var password: String? = null,
    var phone: String? = null,
    var role: Int = 0,
    var isAdmin: Int = 0,
    var belong: Int = -1,
    var createTime: Date?=null
)