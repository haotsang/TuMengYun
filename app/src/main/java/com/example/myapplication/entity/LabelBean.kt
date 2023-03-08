package com.example.myapplication.entity

import java.util.*

data class LabelBean(
    var id: Int = 0,
    var title: String? = null,
    var content: String? = null,
    var visible: Int = 0,
    var pin: String? = null,
    var startTime: Date? = null,
    var endTime: Date? = null,
)