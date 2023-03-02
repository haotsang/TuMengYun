package com.example.myapplication.entity

class ApiResponse<T>(
    var data: T?,
    var errorCode: Int,
    var errorMsg: String
)