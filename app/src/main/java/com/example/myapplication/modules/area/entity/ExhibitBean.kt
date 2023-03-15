package com.example.myapplication.modules.area.entity

data class ExhibitBean(
    val id: Int? = null,
    val name: String? = null,
    val pin: String? = null,
)

data class ExhibitDesc(
    val id: Int? = null,
    val content: String? = null,
    val url: String? = null,
    val parentId: Int? = null
)
