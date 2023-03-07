package com.example.myapplication.entity

import java.util.*

data class UserBean(
    var id: Int? = null,
    var nickname: String? = null,
    var account: String? = null,
    var password: String? = null,
    var phone: String? = null,
    var role: Int = 0,
    var pin: String? = null,
    var createTime: Date?=null,
    var isApply: Int = 0,
    var applyTime: Date?=null,

)