package com.example.myapplication.entity

import java.util.*

data class LabelQuestionBean(
    var id: Int? = null,
    var question: String? = null,
    var answerA: String? = null,
    var answerB: String? = null,
    var answerC: String? = null,
    var rightAnswer: String? = null,

    //解释
    var explanation: String? = null,
    var startTime: Date? = null,
    var endTime: Date? = null,
    var reward: Int? = 0,
    var labelId: Int? = null,


    var alreadyAnswered: Boolean = false,
    //用户选择的答案
    var selectedAnswer: String = "",
)