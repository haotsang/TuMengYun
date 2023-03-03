package com.example.myapplication.entity

data class UserQuestionBean(
    var id: Int? = null,
    var uid: Int? = null,
    var qid: Int? = null,
    //用户选择的答案
    var selectedAnswer: String? = null,
)