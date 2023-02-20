package com.example.myapplication.bean

data class LabelBean(
    var id: Long = 0,
    var title: String? = null,
    var content: String? = null,
    var question: Int = 0,
    var visible: Int = 0,
    var region: Int = 0,
    var type: Int = 0
)